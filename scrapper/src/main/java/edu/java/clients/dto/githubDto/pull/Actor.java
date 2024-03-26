package edu.java.clients.dto.githubDto.pull;

import java.net.URI;
import lombok.Data;

@Data
public class Actor {
    private String login;
    private URI url;
}
