package edu.java.clients;

import edu.java.bot.dto.LinkUpdate;
import edu.java.clients.exceptions.BadQueryParamsException;
import java.net.URI;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

public class BotClient {
    private final static String BASE_URL = "http://localhost:8090";
    private final static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private final WebClient client;

    public BotClient() {
        this.client = WebClient.create(BASE_URL);
    }

    public BotClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public void sendUpdate(Long id, URI url, String description, List<Long> tgChatIds) {
        try {

            client
                .post()
                .uri("/updates")
                .body(Mono.just(new LinkUpdate() {{
                    setId(id);
                    setDescription(description);
                    setTgChatIds(tgChatIds);
                    setUrl(url);
                }}), LinkUpdate.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                    Mono.error(new BadQueryParamsException("Некорректные параметры запроса")))
                .bodyToMono(Void.class)
                .block();

        } catch (WebClientRequestException e) {
            LOGGER.warn("Не возможно установить соединение с bot сервером!");
        }
    }

    public void sendUpdates(List<LinkUpdate> linkUpdateList) {
        linkUpdateList.forEach(entry -> sendUpdate(
            entry.getId(),
            entry.getUrl(),
            entry.getDescription(),
            entry.getTgChatIds()
        ));
    }
}
