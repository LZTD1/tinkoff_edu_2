package edu.java.database.dto;

import edu.java.scrapper.dto.LinkResponse;
import lombok.Data;

@Data
public class UserLinkRel {
    private User user;
    private LinkResponse link;
}
