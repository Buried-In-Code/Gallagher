package github.buriedincode.gallagher.configurations;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor implements Interceptor {
  private final String apiKey;

  @NotNull
  @Override
  public Response intercept(@NotNull Chain chain) throws IOException {
    var request = chain.request();
    var requestTime = System.nanoTime();
    request = request.newBuilder().header("Accept", "*/*").header("Accept-Encoding", "gzip, deflate")
        .header("Authorization", credentials()).header("Connection", "keep-alive")
        .header("Host", request.url().host()).header("User-Agent", "HTTPie/3.2.4").build();
    log.info("Sending request to {}\n{}", request.url(), request.headers());

    var response = chain.proceed(request);

    var responseTime = System.nanoTime();
    log.info(String.format("Received response from %s in %.2fms%n%s", response.request().url(),
        (responseTime - requestTime) / 1e6d, response.headers()));
    return response;
  }

  private String credentials() {
    return Credentials.basic("", apiKey);
  }
}
