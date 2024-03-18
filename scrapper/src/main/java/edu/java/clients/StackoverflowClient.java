package edu.java.clients;

import edu.java.clients.dto.sofDto.stackOverFlowDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackoverflowClient {

    private final static String BASE_URL = "https://api.stackexchange.com";
    private final WebClient client;

    public StackoverflowClient() {
        this.client = WebClient.create(BASE_URL);
    }

    public StackoverflowClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public Mono<stackOverFlowDto> getAnswersByQuestion(int id) {
        return client
            .get()
            .uri(String.format("/2.3/questions/%d/answers?order=desc&sort=creation&site=stackoverflow&filter=withbody", id))
            .retrieve()
            .bodyToMono(stackOverFlowDto.class);
    }

    public Mono<stackOverFlowDto> getCommentsByQuestion(int id) {
        return client
            .get()
            .uri(String.format("/2.3/questions/%d/comments?order=desc&sort=creation&site=stackoverflow&filter=withbody", id))
            .retrieve()
            .bodyToMono(stackOverFlowDto.class);
    }
}
