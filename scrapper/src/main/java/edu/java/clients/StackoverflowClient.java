package edu.java.clients;

import edu.java.clients.dto.sofDto.StackOverFlowDto;
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

    public Mono<StackOverFlowDto> getAnswersByQuestion(int id) {
        return client
            .get()
            .uri(String.format(
                "/2.3/questions/%d/answers?order=desc&sort=creation&site=stackoverflow&filter=withbody",
                id
            ))
            .retrieve()
            .bodyToMono(StackOverFlowDto.class);
    }

    public Mono<StackOverFlowDto> getCommentsByQuestion(int id) {
        return client
            .get()
            .uri(String.format(
                "/2.3/questions/%d/comments?order=desc&sort=creation&site=stackoverflow&filter=withbody",
                id
            ))
            .retrieve()
            .bodyToMono(StackOverFlowDto.class);
    }
}
