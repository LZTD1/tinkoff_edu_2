package edu.java.clients;

import edu.java.clients.dto.githubDto.commit.CommitsDto;
import edu.java.clients.dto.githubDto.pull.PullDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class GithubClient {

    private final static String BASE_URL = "https://api.github.com";
    private final WebClient client;

    public GithubClient() {
        this.client = WebClient.create(BASE_URL);
    }

    public GithubClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public Flux<CommitsDto> getCommitsByRepos(String owner, String repos) {
        return client
            .get()
            .uri(String.format("/repos/%s/%s/commits", owner, repos))
            .retrieve()
            .bodyToFlux(CommitsDto.class);
    }

    public Flux<PullDto> getPullsByRepos(String owner, String repos) {
        return client
            .get()
            .uri(String.format("/repos/%s/%s/events", owner, repos))
            .retrieve()
            .bodyToFlux(PullDto.class);
    }
}
