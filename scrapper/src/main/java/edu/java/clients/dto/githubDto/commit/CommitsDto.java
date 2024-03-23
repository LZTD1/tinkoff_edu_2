package edu.java.clients.dto.githubDto.commit;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.clients.dto.githubDto.UserDto;
import edu.java.clients.dto.githubDto.commit.commit.CommitDto;
import java.net.URI;
import lombok.Data;

@Data
public class CommitsDto {
    @JsonProperty("html_url")
    private URI htmlUrl;

    @JsonProperty("node_id")
    private String nodeId;

    private UserDto author;

    private CommitDto commit;
}
