package edu.java.bot.clients;

import edu.java.bot.clients.exceptions.BadLinkEntityException;
import edu.java.bot.clients.exceptions.BadQueryParamsException;
import edu.java.bot.clients.exceptions.ConflictError;
import edu.java.bot.clients.exceptions.LinkNotFoundException;
import edu.java.scrapper.dto.AddLinkRequest;
import edu.java.scrapper.dto.DeleteLinkRequest;
import edu.java.scrapper.dto.ListLinksResponse;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ScrapperClient {

    @Value("${app.scrapperUrl}")
    private String baseUrl;
    private static final String UNCORRECT_LINK = "Получена ссылка в не верном формате!";
    private final String headerLimit = "limit";
    private final String headerOffset = "offset";
    private final WebClient client;
    private final String linksPath = "/links";
    private final String headerTgChat = "Tg-Chat-Id";
    private final String exceptionMessage = "Некорректные параметры запроса";

    public ScrapperClient() {
        this.client = WebClient.create(baseUrl);
    }

    public ScrapperClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public ListLinksResponse getAllTrackedLinks(Long tgChatId, int limit, int offset) {
        return client
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .build())
            .header(headerTgChat, String.valueOf(tgChatId))
            .header(headerLimit, String.valueOf(limit))
            .header(headerOffset, String.valueOf(offset))
            .retrieve()
            .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.BAD_REQUEST.value(), clientResponse ->
                Mono.error(new BadQueryParamsException(exceptionMessage)))
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public void addTrackLink(Long tgChatId, URI link) {
        client
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .build())
            .header(headerTgChat, String.valueOf(tgChatId))
            .body(Mono.just(new AddLinkRequest() {{
                setLink(link);
            }}), AddLinkRequest.class)
            .retrieve()
            .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.BAD_REQUEST.value(), clientResponse ->
                Mono.error(new BadQueryParamsException(exceptionMessage)))
            .onStatus(
                httpStatusCode -> httpStatusCode.value() == HttpStatus.UNPROCESSABLE_ENTITY.value(),
                clientResponse ->
                    Mono.error(new BadLinkEntityException(UNCORRECT_LINK))
            )
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteTrackLink(Long tgChatId, URI link) {
        client
            .method(HttpMethod.DELETE)
            .uri(uriBuilder -> uriBuilder
                .path(linksPath)
                .build())
            .header(headerTgChat, String.valueOf(tgChatId))
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

    public void registerUser(Long tgChatId) {
        client
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/tg-chat/{id}")
                .build(tgChatId))
            .retrieve()
            .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.BAD_REQUEST.value(), clientResponse ->
                Mono.error(new BadQueryParamsException(exceptionMessage)))
            .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.NOT_FOUND.value(), clientResponse ->
                Mono.error(new LinkNotFoundException()))
            .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.CONFLICT.value(), clientResponse ->
                Mono.error(new ConflictError("Данный юзер уже зарегистрирован!")))
            .bodyToMono(Void.class)
            .block();
    }
}
