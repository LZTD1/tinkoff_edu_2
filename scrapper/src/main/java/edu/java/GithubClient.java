package edu.java;

import edu.java.entity.git.GitResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class GithubClient {

    private final WebClient client;

    public GithubClient() {
        this.client = WebClient.create("https://api.github.com");
    }

    public GithubClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public Flux<GitResponse> getAnswersByQuestion(String userName, String repos, int id) {
        return client
            .get()
            .uri("/repos/" + userName + "/" + repos + "/issues/" + id + "/comments")
            .retrieve()
            .bodyToFlux(GitResponse.class);
    }
}
