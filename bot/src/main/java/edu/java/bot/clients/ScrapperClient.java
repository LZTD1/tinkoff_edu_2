package edu.java.bot.clients;

import edu.java.bot.dto.LinkUpdate;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final static String BASE_URL = "http://localhost:80980";
    private final WebClient client;

    public ScrapperClient() {
        this.client = WebClient.create(BASE_URL);
    }

    public ScrapperClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public Mono<ResponseEntity<Void>> getAnswersByQuestion(Long id, URI url, String description, List<Long> tgChatIds) {
        return client
            .post()
            .uri("/updates")
            .body(Mono.just(new LinkUpdate() {{
                setId(id);
                setDescription(description);
                setTgChatIds(tgChatIds);
                setUrl(url);
            }}), LinkUpdate.class)
            .retrieve()
            .toBodilessEntity();
    }
}
