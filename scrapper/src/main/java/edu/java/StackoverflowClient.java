package edu.java;

import edu.java.entity.sof.SofResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackoverflowClient {

    private final WebClient client;

    public StackoverflowClient() {
        this.client = WebClient.create("https://api.stackexchange.com");
    }

    public StackoverflowClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public Mono<SofResponse> getAnswersByQuestion(int id) {
        return client
            .get()
            .uri("/2.3/questions/" + id + "/answers?order=desc&sort=activity&site=stackoverflow")
            .retrieve()
            .bodyToMono(SofResponse.class);
    }
}
