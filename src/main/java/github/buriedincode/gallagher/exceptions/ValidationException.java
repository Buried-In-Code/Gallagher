package github.buriedincode.gallagher.exceptions;

public class ValidationException extends UnexpectedException {
  public ValidationException() {
    super();
  }

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
