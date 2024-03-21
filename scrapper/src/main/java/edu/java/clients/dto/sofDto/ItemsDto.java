package edu.java.clients.dto.sofDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class ItemsDto {

    private OwnerSofDto owner;

    private String body;

    @JsonProperty("creation_date")
    private OffsetDateTime creationDate;
}
