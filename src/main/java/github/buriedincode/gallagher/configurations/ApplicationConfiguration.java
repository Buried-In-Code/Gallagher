package github.buriedincode.gallagher.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.*;
import okhttp3.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
  @Value("${gallagher.api-key}")
  private String apiKey;

  @Value("${gallagher.cert-path}")
  private String certPath;

  @Value("${gallagher.key-path}")
  private String keyPath;

  @Bean
  public Interceptor authenticationInterceptor() {
    return new AuthenticationInterceptor(apiKey);
  }

  @Bean("httpClient")
  @Profile("default")
  public OkHttpClient defaultHttpClient(Interceptor authenticationInterceptor) {
    Security.addProvider(new BouncyCastleProvider());
    var keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(null, null);

    var certificateFactory = CertificateFactory.getInstance("X.509");
    try (InputStream certInputStream = new FileInputStream(certPath)) {
      var clientCert = certificateFactory.generateCertificate(certInputStream);
      keyStore.setCertificateEntry("client-cert", clientCert);
    }

    var privateKey = loadPrivateKey();
    keyStore.setKeyEntry("client-key", privateKey, "password".toCharArray(),
        new Certificate[]{keyStore.getCertificate("client-cert")});

    var keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
    keyManagerFactory.init(keyStore, "password".toCharArray());

    var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init((KeyStore) null);

    var sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

    return new OkHttpClient.Builder().addInterceptor(authenticationInterceptor)
        .sslSocketFactory(sslContext.getSocketFactory(),
            (X509TrustManager) trustManagerFactory.getTrustManagers()[0])
        .connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS).build();
  }

  @Bean("httpClient")
  @Profile("local")
  public OkHttpClient localHttpClient(Interceptor authenticationInterceptor) {
    return new OkHttpClient.Builder().addInterceptor(authenticationInterceptor).connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build();
  }

  public PrivateKey loadPrivateKey() throws IOException {
    try (FileReader keyReader = new FileReader(keyPath)) {

      var pemParser = new PEMParser(keyReader);
      var converter = new JcaPEMKeyConverter().setProvider("BC");
      var object = pemParser.readObject();
      var kp = converter.getKeyPair((PEMKeyPair) object);
      return kp.getPrivate();
    }
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper().registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
