package github.buriedincode.gallagher.exceptions;

public class ForbiddenException extends UnexpectedException {
  public ForbiddenException() {
    super();
  }

  public ForbiddenException(String message) {
    super(message);
  }

  public ForbiddenException(String message, Throwable cause) {
    super(message, cause);
  }
}
