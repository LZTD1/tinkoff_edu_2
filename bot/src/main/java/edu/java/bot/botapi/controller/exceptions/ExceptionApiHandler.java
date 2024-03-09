package edu.java.bot.botapi.controller.exceptions;

import edu.java.shared.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(EmptyIdListException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse notFoundException(EmptyIdListException exception) {
        return new ApiErrorResponse() {{
            setCode(HttpStatus.BAD_REQUEST.toString());
            setExceptionName("EmptyIdListException");
            setExceptionMessage(exception.getMessage());
        }};
    }
}
