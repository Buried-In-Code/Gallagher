package github.buriedincode.gallagher.configurations;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor implements Interceptor {
  private final String apiKey;
  private final boolean debug;

  @NotNull
  @Override
  public Response intercept(@NotNull Chain chain) throws IOException {
    var request = chain.request();
    var requestTime = System.nanoTime();
    request = request.newBuilder().header("Authorization", credentials()).build();
    log.info("Sending request to {}", request.url());
    if (debug)
      log.debug("Details: {}\n{}", request.headers(),
          request.body() == null ? null : stringifyRequestBody(request));

    var response = chain.proceed(request);

    var responseTime = System.nanoTime();
    log.info("Received response from %s in %.2fms".formatted(response.request().url(),
        (responseTime - requestTime) / 1e6d));
    return response;
  }

  private String credentials() {
    return Credentials.basic("", apiKey);
  }

  private String stringifyRequestBody(Request request) {
    try {
      final Request copy = request.newBuilder().build();
      final Buffer buffer = new Buffer();
      copy.body().writeTo(buffer);
      return buffer.readUtf8();
    } catch (IOException ioe) {
      return "Failed to read request body";
    }
  }
}
