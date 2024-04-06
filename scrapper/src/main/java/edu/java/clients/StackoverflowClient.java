package edu.java.clients;

import edu.java.clients.dto.sofDto.StackOverFlowDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;

public class StackoverflowClient {
    private final static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private final static String BASE_URL = "https://api.stackexchange.com";
    private final WebClient client;

    @Autowired
    private RetryTemplate retryTemplate;

    public StackoverflowClient() {
        this.client = WebClient.create(BASE_URL);
    }

    public StackoverflowClient(String baseUrl, RetryTemplate retryTemplate) {
        this.client = WebClient.create(baseUrl);
        this.retryTemplate = retryTemplate;
    }

    public StackOverFlowDto getAnswersByQuestion(int id) {
        LOGGER.info("[SOF] Парсинг ответов, вопрос ID - {}", id);
        return retryTemplate.execute((context) -> {
            warnIfRetry(id, context, "ответов");
            return client
                .get()
                .uri(String.format(
                    "/2.3/questions/%d/answers?order=desc&sort=creation&site=stackoverflow&filter=withbody",
                    id
                ))
                .retrieve()
                .bodyToMono(StackOverFlowDto.class)
                .block();
        });
    }

    public StackOverFlowDto getCommentsByQuestion(int id) {
        LOGGER.info("[SOF] Парсинг комментов, вопрос ID - {}", id);
        return retryTemplate.execute((context) -> {
            warnIfRetry(id, context, "комментов");
            return client
                .get()
                .uri(String.format(
                    "/2.3/questions/%d/comments?order=desc&sort=creation&site=stackoverflow&filter=withbody",
                    id
                ))
                .retrieve()
                .bodyToMono(StackOverFlowDto.class)
                .block();
        });
    }

    private void warnIfRetry(int id, RetryContext context, String target) {
        if (context.getRetryCount() > 0) {
            LOGGER.warn(
                "[SOF] Парсинг {}, вопрос ID - {} ({} попытка)",
                target,
                id,
                context.getRetryCount() + 1
            );
        }
    }
}
