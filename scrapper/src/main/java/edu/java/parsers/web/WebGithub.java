package edu.java.parsers.web;

import edu.java.clients.GithubClient;
import edu.java.clients.dto.githubDto.GitResponseDto;
import edu.java.database.dto.Link;
import edu.java.parsers.WebHandler;
import java.net.URI;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class WebGithub implements WebHandler {

    private GithubClient githubClient;

    public WebGithub(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    @Override
    public String getHost() {
        return "api.github.com";
    }

    @Override
    public void getUpdate(Link link) {
        GitResponseDto result = getGitResponseDto(link);

//        if (!link.getLasthash().equals(result.nodeId)) {
//            linkService.updateTimeAndLastHash(entry.getId(), result.nodeId);
//            botClient.sendUpdate(
//                entry.getId(),
//                entry.getLink(),
//                String.format(
//                    "commit\nMessage - %s\n,Created by: %s (%s)",
//                    result.commit.message,
//                    result.commit.committer.name,
//                    result.commit.committer.email
//                ),
//                linkService.getAllUsersWithLink(entry)
//            );
//        }
    }

    private GitResponseDto getGitResponseDto(Link link) {
        return githubClient.getAnswersByQuestion(
            getOwner(link.getLink().getPath()),
            getRepos(link.getLink().getPath())
        ).collectList().block().getFirst();
    }

    @Override
    public Optional<String> getUpdateReasonIfHas(URI uri) {
        return Optional.empty();
    }

    private String getRepos(String path) {
        return path.split("/")[2];
    }

    private String getOwner(String path) {
        return path.split("/")[1];
    }
}
