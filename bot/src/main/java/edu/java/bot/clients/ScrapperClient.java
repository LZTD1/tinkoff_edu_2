package edu.java.bot.clients;

import edu.java.scrapper.dto.AddLinkRequest;
import edu.java.scrapper.dto.DeleteLinkRequest;
import edu.java.scrapper.dto.ListLinksResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final static String BASE_URL = "http://localhost:8080";
    private final WebClient client;
    private final String linksPath = "/links";
    private final String queryTgChat = "Tg-Chat-Id";

    public ScrapperClient() {
        this.client = WebClient.create(BASE_URL);
    }

    public ScrapperClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public Mono<ListLinksResponse> getAllTrackedLinks(Long tgChatId) {
        return client
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .queryParam(queryTgChat, tgChatId)
                .build())
            .retrieve()
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<ResponseEntity<Void>> addTrackLink(Long tgChatId, URI link) {
        return client
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .queryParam(queryTgChat, tgChatId)
                .build())
            .body(Mono.just(new AddLinkRequest() {{
                setLink(link);
            }}), AddLinkRequest.class)
            .retrieve()
            .toBodilessEntity();
    }

    public Mono<ResponseEntity<Void>> deleteTrackLink(Long tgChatId, URI link) {
        return client
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .queryParam(queryTgChat, tgChatId)
                .build())
            .body(Mono.just(new DeleteLinkRequest() {{
                setLink(link);
            }}), DeleteLinkRequest.class)
            .retrieve()
            .toBodilessEntity();
    }
}
