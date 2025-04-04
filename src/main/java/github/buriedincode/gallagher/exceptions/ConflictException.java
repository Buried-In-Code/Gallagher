package github.buriedincode.gallagher.exceptions;

public class ConflictException extends UnexpectedException {
  public ConflictException() {
    super();
  }

  public ConflictException(String message) {
    super(message);
  }

  public ConflictException(String message, Throwable cause) {
    super(message, cause);
  }
}
