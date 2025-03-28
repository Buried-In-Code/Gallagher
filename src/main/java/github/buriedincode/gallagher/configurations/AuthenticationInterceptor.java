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
    request = request.newBuilder().header("Authorization", credentials()).build();
    log.info("Sending request to {}", request.url());

    var response = chain.proceed(request);

    var responseTime = System.nanoTime();
    log.info(String.format("Received response from %s in %.2fms", response.request().url(),
        (responseTime - requestTime) / 1e6d));
    return response;
  }

  private String credentials() {
    return Credentials.basic("", apiKey);
  }
}
