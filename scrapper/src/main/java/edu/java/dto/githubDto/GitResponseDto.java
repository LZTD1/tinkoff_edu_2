package edu.java.dto.githubDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitResponseDto {
    public String sha;
    @JsonProperty("node_id")
    public String nodeId;

    @Override public String toString() {
        return "GitResponseDto{"
            + "sha='" + sha + '\''
            + ", nodeId='" + nodeId + '\''
            + '}';
    }
}
