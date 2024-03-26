package edu.java.clients.dto.githubDto.commit;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import lombok.Data;

@Data
public class UserDto {

    private String login;

    private Long id;

    @JsonProperty("html_url")
    private URI htmlUrl;
}
