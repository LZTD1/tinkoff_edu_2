package edu.java.scrapper.dto;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Data;

@Data
public class ListLinksResponse {

    @Valid
    private List<@Valid LinkResponse> links;

    private Integer size;
}

