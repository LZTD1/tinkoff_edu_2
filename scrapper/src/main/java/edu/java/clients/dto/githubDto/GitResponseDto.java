package edu.java.clients.dto.githubDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitResponseDto {
    public String sha;
    @JsonProperty("node_id")
    public String nodeId;

    public CommitDto commit;
}
