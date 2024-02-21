package edu.java.entity.git;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    public String login;
    public int id;
    @JsonProperty("node_id")
    public String nodeId;
    @JsonProperty("avatar_url")
    public String avatarUrl;
    public String url;

    @Override public String toString() {
        return "User{"
            + "login='" + login + '\''
            + ", id=" + id
            + ", nodeId='" + nodeId + '\''
            + ", avatarUrl='" + avatarUrl + '\''
            + ", url='" + url + '\''
            + '}';
    }
}
