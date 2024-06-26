package edu.java.scrapperapi.exceptions;

import edu.java.shared.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    private static List<String> getStackTraceList(RuntimeException exception) {
        return Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString)
            .toList();
    }

    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse notFoundException(LinkNotFoundException exception) {
        return new ApiErrorResponse() {{
            setCode(HttpStatus.CONFLICT.toString());
            setExceptionName("LinkNotFoundException");
            setStacktrace(getStackTraceList(exception));
            setExceptionMessage(exception.getMessage());
        }};
    }

    @ExceptionHandler(TooManyRequests.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiErrorResponse tooManyRequests(TooManyRequests exception) {
        return new ApiErrorResponse() {{
            setCode(HttpStatus.TOO_MANY_REQUESTS.toString());
            setExceptionName("tooManyRequests");
            setStacktrace(getStackTraceList(exception));
            setExceptionMessage(exception.getMessage());
        }};
    }

    @ExceptionHandler(LinkIsNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiErrorResponse linkIsNotValid(LinkIsNotValidException exception) {
        return new ApiErrorResponse() {{
            setCode(HttpStatus.UNPROCESSABLE_ENTITY.toString());
            setExceptionName("linkIsNotValid");
            setStacktrace(getStackTraceList(exception));
            setExceptionMessage(exception.getMessage());
        }};
    }

    @ExceptionHandler(EntityAlreadyExistsError.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse entityAlreadyExists(EntityAlreadyExistsError exception) {
        return new ApiErrorResponse() {{
            setCode(HttpStatus.CONFLICT.toString());
            setExceptionName("entityAlreadyExists");
            setStacktrace(getStackTraceList(exception));
            setExceptionMessage(exception.getMessage());
        }};
    }

    @ExceptionHandler(EntityDeleteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse entityAlreadyDelete(EntityDeleteException exception) {
        return new ApiErrorResponse() {{
            setCode(HttpStatus.CONFLICT.toString());
            setExceptionName("entityAlreadyDelete");
            setStacktrace(getStackTraceList(exception));
            setExceptionMessage(exception.getMessage());
        }};
    }

    @ExceptionHandler(UserIsNotDefindedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse userIsNotDefinded(UserIsNotDefindedException exception) {
        return new ApiErrorResponse() {{
            setCode(HttpStatus.NOT_FOUND.toString());
            setExceptionName("userIsNotDefinded");
            setExceptionMessage(exception.getMessage());
            setStacktrace(getStackTraceList(exception));
            setDescription("Операция, производимая над пользователем, не нашла конкретного пользователя!");
        }};
    }

    @ExceptionHandler(LinkAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse alreadyExistsException(LinkAlreadyExistsException exception) {
        return new ApiErrorResponse() {{
            setCode(HttpStatus.CONFLICT.toString());
            setExceptionName("LinkAlreadyExistsException");
            setStacktrace(getStackTraceList(exception));
            setExceptionMessage(exception.getMessage());
        }};
    }
}
