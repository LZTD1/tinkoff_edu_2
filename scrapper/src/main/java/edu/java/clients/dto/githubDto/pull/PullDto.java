package edu.java.clients.dto.githubDto.pull;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.clients.dto.githubDto.UserDto;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class PullDto {

    @JsonProperty("html_url")
    private URI htmlUrl;

    private Long id;

    private String body;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    private UserDto user;
}
