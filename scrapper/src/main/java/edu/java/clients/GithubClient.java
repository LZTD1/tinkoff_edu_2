package edu.java.clients;

import edu.java.clients.dto.githubDto.commit.CommitsDto;
import edu.java.clients.dto.githubDto.pull.PullDto;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClient {
    private final static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private final static String BASE_URL = "https://api.github.com";
    private final WebClient client;

    @Autowired
    private RetryTemplate retryTemplate;

    public GithubClient() {
        this.client = WebClient.create(BASE_URL);
    }

    public GithubClient(String baseUrl, RetryTemplate retryTemplate) {
        this.client = WebClient.create(baseUrl);
        this.retryTemplate = retryTemplate;
    }

    public List<CommitsDto> getCommitsByRepos(String owner, String repos) {
        LOGGER.info("[GITHUB] Парсинг коммитов, репозиторий - {} ", repos);
        return retryTemplate.execute((context) -> {
            warnIfRetry(repos, context, "коммитов");
            return client
                .get()
                .uri(String.format("/repos/%s/%s/commits", owner, repos))
                .retrieve()
                .bodyToFlux(CommitsDto.class)
                .collectList()
                .block();
        });
    }

    public List<PullDto> getPullsByRepos(String owner, String repos) {
        LOGGER.info("[GITHUB] Парсинг пуллов, репозиторий - {}", repos);
        return retryTemplate.execute((context) -> {
            warnIfRetry(repos, context, "пуллов");
            return client
                .get()
                .uri(String.format("/repos/%s/%s/events", owner, repos))
                .retrieve()
                .bodyToFlux(PullDto.class)
                .collectList()
                .block();
        });
    }

    private void warnIfRetry(String repos, RetryContext context, String target) {
        if (context.getRetryCount() > 0) {
            LOGGER.warn(
                "[GITHUB] Повторный парсинг {}, репозиторий - {} ({} попытка)",
                target,
                repos,
                context.getRetryCount() + 1
            );
        }
    }
}
