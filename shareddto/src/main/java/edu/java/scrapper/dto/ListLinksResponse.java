package edu.java.scrapper.dto;

import jakarta.validation.Valid;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ListLinksResponse {

    @Valid
    private List<@Valid LinkResponse> links;

    private Integer size;
}

