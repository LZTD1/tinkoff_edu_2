package edu.java.bot.botapi.controller.exceptions;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(EmptyIdListException.class)
    public ResponseEntity<ErrorMessage> notFoundException(EmptyIdListException exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorMessage(exception.getMessage()));
    }
}
