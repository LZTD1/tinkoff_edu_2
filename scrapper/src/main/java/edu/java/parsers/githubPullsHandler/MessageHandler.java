package edu.java.parsers.githubPullsHandler;

import edu.java.clients.dto.githubDto.pull.PullDto;

@FunctionalInterface
public interface MessageHandler {
    String getMessage(PullDto entry);
}
