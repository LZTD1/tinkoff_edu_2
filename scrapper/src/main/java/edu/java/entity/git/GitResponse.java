package edu.java.entity.git;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class GitResponse {
    public String url;
    @JsonProperty("issue_url")
    public String issueUrl;
    public int id;
    @JsonProperty("node_id")
    public String nodeId;
    public User user;
    @JsonProperty("created_at")
    public OffsetDateTime createdAt;
    @JsonProperty("updated_at")
    public OffsetDateTime updatedAt;

    @Override public String toString() {
        return "GitResponse{"
            + "url='" + url + '\''
            + ", issueUrl='" + issueUrl + '\''
            + ", id=" + id
            + ", nodeId='" + nodeId + '\''
            + ", user=" + user
            + ", createdAt=" + createdAt
            + ", updatedAt=" + updatedAt
            + '}';
    }
}
