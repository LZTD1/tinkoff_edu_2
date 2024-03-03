package edu.java.bot.clients;

import edu.java.bot.clients.exceptions.BadQueryParamsException;
import edu.java.bot.clients.exceptions.LinkNotFoundException;
import edu.java.bot.clients.exceptions.UnexpectedCodeException;
import edu.java.scrapper.dto.AddLinkRequest;
import edu.java.scrapper.dto.DeleteLinkRequest;
import edu.java.scrapper.dto.ListLinksResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final static String BASE_URL = "http://localhost:8080";
    private final WebClient client;
    private final String linksPath = "/links";
    private final String queryTgChat = "Tg-Chat-Id";
    private final Map<Integer, Supplier<Void>> errorMap = new HashMap<>() {{
        put(HttpStatus.OK.value(), () -> null);
        put(HttpStatus.BAD_REQUEST.value(), () -> {
            throw new BadQueryParamsException("Некорректные параметры запроса");
        });
        put(HttpStatus.NOT_FOUND.value(), () -> {
            throw new LinkNotFoundException();
        });
    }};

    public ScrapperClient() {
        this.client = WebClient.create(BASE_URL);
    }

    public ScrapperClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public ListLinksResponse getAllTrackedLinks(Long tgChatId) {
        ResponseEntity<ListLinksResponse> response = client
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .queryParam(queryTgChat, tgChatId)
                .build())
            .retrieve()
            .toEntity(ListLinksResponse.class)
            .block();

        var code = response.getStatusCode().value();
        this.errorMap.getOrDefault(code, () -> {
            throw new UnexpectedCodeException(code);
        }).get();

        return response.getBody();
    }

    public void addTrackLink(Long tgChatId, URI link) {
        int responseCode = client
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .queryParam(queryTgChat, tgChatId)
                .build())
            .body(Mono.just(new AddLinkRequest() {{
                setLink(link);
            }}), AddLinkRequest.class)
            .retrieve()
            .toBodilessEntity()
            .block()
            .getStatusCode()
            .value();

        this.errorMap.getOrDefault(responseCode, () -> {
            throw new UnexpectedCodeException(responseCode);
        }).get();
    }

    public void deleteTrackLink(Long tgChatId, URI link) {
        int responseCode = client
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .queryParam(queryTgChat, tgChatId)
                .build())
            .body(Mono.just(new DeleteLinkRequest() {{
                setLink(link);
            }}), DeleteLinkRequest.class)
            .retrieve()
            .toBodilessEntity()
            .block()
            .getStatusCode()
            .value();

        this.errorMap.getOrDefault(responseCode, () -> {
            throw new UnexpectedCodeException(responseCode);
        }).get();
    }
}
