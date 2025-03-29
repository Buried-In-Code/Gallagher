package github.buriedincode.gallagher.exceptions;

import github.buriedincode.gallagher.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception) {
    return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflictException(ConflictException exception) {
    return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException exception) {
    return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
    return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UnexpectedException.class)
  public ResponseEntity<ErrorResponse> handleUnexpectedException(UnexpectedException exception) {
    return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(ValidationException exception) {
    return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
