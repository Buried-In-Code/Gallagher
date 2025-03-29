package github.buriedincode.gallagher.exceptions;

public class BadRequestException extends UnexpectedException {
  public BadRequestException() {
    super();
  }

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
