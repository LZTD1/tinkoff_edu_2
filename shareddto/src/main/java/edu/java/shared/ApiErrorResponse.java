package edu.java.shared;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Data;

@Data
public class ApiErrorResponse {

    private String description;

    private String code;

    private String exceptionName;

    private String exceptionMessage;

    @Valid
    private List<String> stacktrace;
}

