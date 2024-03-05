package edu.java.bot.clients;

import edu.java.bot.clients.exceptions.BadQueryParamsException;
import edu.java.bot.clients.exceptions.LinkNotFoundException;
import edu.java.scrapper.dto.AddLinkRequest;
import edu.java.scrapper.dto.DeleteLinkRequest;
import edu.java.scrapper.dto.ListLinksResponse;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final static String BASE_URL = "http://localhost:8080";
    private final WebClient client;
    private final String linksPath = "/links";
    private final String queryTgChat = "Tg-Chat-Id";
    private final String exceptionMessage = "Некорректные параметры запроса";

    public ScrapperClient() {
        this.client = WebClient.create(BASE_URL);
    }

    public ScrapperClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public ListLinksResponse getAllTrackedLinks(Long tgChatId) {
        ListLinksResponse response = client
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .queryParam(queryTgChat, tgChatId)
                .build())
            .retrieve()
            .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.BAD_REQUEST.value(), clientResponse ->
                Mono.error(new BadQueryParamsException(exceptionMessage)))
            .bodyToMono(ListLinksResponse.class)
            .block();

        return response;
    }

    public void addTrackLink(Long tgChatId, URI link) {
        client
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .queryParam(queryTgChat, tgChatId)
                .build())
            .body(Mono.just(new AddLinkRequest() {{
                setLink(link);
            }}), AddLinkRequest.class)
            .retrieve()
            .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.BAD_REQUEST.value(), clientResponse ->
                Mono.error(new BadQueryParamsException(exceptionMessage)))
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteTrackLink(Long tgChatId, URI link) {
        client
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .queryParam(queryTgChat, tgChatId)
                .build())
            .body(Mono.just(new DeleteLinkRequest() {{
                setLink(link);
            }}), DeleteLinkRequest.class)
            .retrieve()
            .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.BAD_REQUEST.value(), clientResponse ->
                Mono.error(new BadQueryParamsException(exceptionMessage)))
            .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.NOT_FOUND.value(), clientResponse ->
                Mono.error(new LinkNotFoundException()))
            .bodyToMono(Void.class)
            .block();
    }
}
