package github.buriedincode.gallagher.models;

import java.time.LocalDateTime;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public final class ErrorResponse {
  @Nullable
  private final String message;
  @NotNull
  private final String timestamp;

  public ErrorResponse() {
    this(null);
  }

  public ErrorResponse(@Nullable String message) {
    this.message = message;
    this.timestamp = LocalDateTime.now().toString();
  }
}
