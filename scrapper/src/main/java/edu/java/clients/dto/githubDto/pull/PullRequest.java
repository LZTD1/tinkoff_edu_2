package edu.java.clients.dto.githubDto.pull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PullRequest {
    private int comments;
    @JsonProperty("review_comments")
    private int reviewComments;
    private int commits;
    private String title;

    @JsonProperty("merged_by")
    private MergedBy mergedBy;

    private Head head;
}
