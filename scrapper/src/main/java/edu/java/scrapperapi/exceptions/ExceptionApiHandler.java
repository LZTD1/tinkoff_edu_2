package edu.java.scrapperapi.exceptions;

import edu.java.shared.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse notFoundException(LinkNotFoundException exception) {
        return new ApiErrorResponse() {{
            setCode(HttpStatus.CONFLICT.toString());
            setExceptionName("LinkNotFoundException");
            setExceptionMessage(exception.getMessage());
        }};
    }

    @ExceptionHandler(LinkAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse alreadyExistsException(LinkAlreadyExistsException exception) {
        return new ApiErrorResponse() {{
            setCode(HttpStatus.CONFLICT.toString());
            setExceptionName("LinkAlreadyExistsException");
            setExceptionMessage(exception.getMessage());
        }};
    }
}
