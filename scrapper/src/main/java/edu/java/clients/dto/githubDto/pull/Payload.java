package edu.java.clients.dto.githubDto.pull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Payload {

    // at created \\
    private String ref;
    @JsonProperty("master_branch")
    private String masterBranch;

    // at merged \\
    @JsonProperty("pull_request")
    private PullRequest pullRequest;
    private String action;

}
