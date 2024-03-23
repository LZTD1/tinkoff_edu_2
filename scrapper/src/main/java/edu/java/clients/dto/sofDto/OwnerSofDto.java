package edu.java.clients.dto.sofDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import lombok.Data;

@Data
public class OwnerSofDto {

    @JsonProperty("display_name")
    private String displayName;

    private URI link;

    private Long reputation;
}
