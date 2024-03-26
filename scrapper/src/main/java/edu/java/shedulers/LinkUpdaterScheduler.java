package edu.java.shedulers;

import edu.java.bot.dto.LinkUpdate;
import edu.java.clients.BotClient;
import edu.java.clients.GithubClient;
import edu.java.clients.StackoverflowClient;
import edu.java.dto.Link;
import edu.java.parsers.WebHandler;
import edu.java.scrapperapi.services.LinkService;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "app.scheduler", name = "enable", havingValue = "true")
public class LinkUpdaterScheduler {

    private final static Logger LOGGER = LogManager.getLogger();
    private final List<WebHandler> webHandlers;
    private LinkService linkService;
    private GithubClient githubClient;
    private StackoverflowClient stackoverflowClient;
    private BotClient botClient;

    @Value("${app.linkDelay}")
    private int linkDelay;

    @Value("${app.limitPerCheck}")
    private int limitPerCheck;

    @Autowired
    public LinkUpdaterScheduler(
        LinkService linkService,
        GithubClient githubClient,
        BotClient botClient,
        StackoverflowClient stackoverflowClient,
        List<WebHandler> webHandlers
    ) {
        this.linkService = linkService;
        this.githubClient = githubClient;
        this.stackoverflowClient = stackoverflowClient;
        this.botClient = botClient;
        this.webHandlers = webHandlers;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    @SuppressWarnings("LambdaBodyLength")
    public void update() {
        LOGGER.info("Updating...");
        List<Link> list = linkService.listScheduler(linkDelay, limitPerCheck);
        Map<String, WebHandler> handlerContainer = getHandlerContainer();
        list.forEach(
            entry -> {
                WebHandler webHandler = handlerContainer.get(entry.getLink().getHost());
                List<LinkUpdate> listUpdates = webHandler.getUpdate(entry);
                botClient.sendUpdates(listUpdates);
            }
        );
    }

    private Map<String, WebHandler> getHandlerContainer() {
        return this.webHandlers.stream().collect(
            Collectors.toMap(WebHandler::getHost, Function.identity())
        );
    }
}
