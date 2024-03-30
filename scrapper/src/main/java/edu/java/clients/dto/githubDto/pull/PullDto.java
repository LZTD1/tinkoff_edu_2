package edu.java.clients.dto.githubDto.pull;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class PullDto {

    private String type; // PullRequestEvent or CreateEvent

    private Payload payload;

    private Repo repo;

    private Actor actor;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
}
