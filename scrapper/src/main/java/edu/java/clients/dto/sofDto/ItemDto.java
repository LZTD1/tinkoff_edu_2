package edu.java.clients.dto.sofDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemDto {
    public OwnerDto owner;
    @JsonProperty("is_accepted")
    public boolean isAccepted;
    public int score;
    @JsonProperty("last_activity_date")
    public int lastActivityDate;
    @JsonProperty("creation_date")
    public int creationDate;
    @JsonProperty("answer_id")
    public int answerId;
    @JsonProperty("question_id")
    public int questionId;
    @JsonProperty("content_license")
    public String contentLicense;

    @Override public String toString() {
        return "Item{"
            + "owner=" + owner
            + ", is_accepted=" + isAccepted
            + ", score=" + score
            + ", last_activity_date=" + lastActivityDate
            + ", creation_date=" + creationDate
            + ", answer_id=" + answerId
            + ", question_id=" + questionId
            + ", content_license='" + contentLicense + '\''
            + '}';
    }
}
