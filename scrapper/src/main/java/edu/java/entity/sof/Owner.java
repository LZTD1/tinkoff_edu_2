package edu.java.entity.sof;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Owner {
    @JsonProperty("account_id")
    public int accountId;
    public int reputation;
    @JsonProperty("user_id")
    public int userId;
    @JsonProperty("user_type")
    public String userType;
    @JsonProperty("profile_image")
    public String profileImage;
    @JsonProperty("display_name")
    public String displayName;
    public String link;

    @Override public String toString() {
        return "Owner{"
            + "account_id=" + accountId
            + ", reputation=" + reputation
            + ", user_id=" + userId
            + ", user_type='" + userType + '\''
            + ", profile_image='" + profileImage + '\''
            + ", display_name='" + displayName + '\''
            + ", link='" + link + '\''
            + '}';
    }
}
