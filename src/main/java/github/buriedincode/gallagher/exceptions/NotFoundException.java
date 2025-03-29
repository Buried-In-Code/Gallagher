package github.buriedincode.gallagher.exceptions;

public class NotFoundException extends UnexpectedException {
  public NotFoundException() {
    super();
  }

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
