package edu.java.clients;

import edu.java.bot.dto.LinkUpdate;
import edu.java.clients.exceptions.BadQueryParamsException;
import edu.java.clients.exceptions.UnexpectedCodeException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    private final static String BASE_URL = "http://localhost:8090";
    private final WebClient client;
    private final Map<Integer, Supplier<Void>> errorMap = new HashMap<>() {{
        put(HttpStatus.OK.value(), () -> null);
        put(HttpStatus.BAD_REQUEST.value(), () -> {
            throw new BadQueryParamsException("Некорректные параметры запроса");
        });
    }};

    public BotClient() {
        this.client = WebClient.create(BASE_URL);
    }

    public BotClient(String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public void sendUpdate(Long id, URI url, String description, List<Long> tgChatIds) {
        var code = client
            .post()
            .uri("/updates")
            .body(Mono.just(new LinkUpdate() {{
                setId(id);
                setDescription(description);
                setTgChatIds(tgChatIds);
                setUrl(url);
            }}), LinkUpdate.class)
            .retrieve()
            .toBodilessEntity()
            .block()
            .getStatusCode()
            .value();

        this.errorMap.getOrDefault(code, () -> {
            throw new UnexpectedCodeException(code);
        }).get();
    }
}
