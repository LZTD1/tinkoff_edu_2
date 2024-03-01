package edu.java.scrapperapi.exceptions;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(LinkNotFoundException exception) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(LinkAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> alreadyExistsException(LinkAlreadyExistsException exception) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorMessage(exception.getMessage()));
    }
}
