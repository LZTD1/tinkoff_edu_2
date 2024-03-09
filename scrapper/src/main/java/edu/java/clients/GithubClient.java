package edu.java.clients;

import edu.java.clients.dto.githubDto.GitResponseDto;
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

    public Flux<GitResponseDto> getAnswersByQuestion(String owner, String repos) {
        return client
            .get()
            .uri(String.format("/repos/%s/%s/commits", owner, repos))
            .retrieve()
            .bodyToFlux(GitResponseDto.class);
    }
}
