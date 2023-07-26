package spring.rest.serversideapp.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import spring.rest.serversideapp.util.ErrorResponse;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityHasAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> EntityHasAlreadyExistHandler(EntityHasAlreadyExistException exception) {
        final ErrorResponse response = new ErrorResponse(
                exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> EntityNotFoundHandler(EntityNotFoundException exception) {
        final ErrorResponse response = new ErrorResponse(
                exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<ErrorResponse> EntityValidationHandler(EntityValidationException exception) {
        final ErrorResponse response = new ErrorResponse(
                exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
