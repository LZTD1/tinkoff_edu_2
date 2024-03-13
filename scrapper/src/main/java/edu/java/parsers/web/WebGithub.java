package edu.java.parsers.web;

import edu.java.clients.GithubClient;
import edu.java.clients.dto.githubDto.GitResponseDto;
import edu.java.parsers.WebHandler;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebGithub implements WebHandler {

    @Autowired
    private GithubClient githubClient;

    @Override
    public String getHost() {
        return "api.github.com";
    }

    public Optional<String> getUpdateReasonIfHas(
        URI uri
    ) {
        List<GitResponseDto> response = githubClient.getAnswersByQuestion(
                getOwner(uri.getPath()),
                getRepos(uri.getPath())
            )
            .collectList()
            .block();

        return Optional.of(response.toString());
    }

    private String getRepos(String path) {
        return path.split("/")[3];
    }

    private String getOwner(String path) {
        return path.split("/")[2];
    }

}
