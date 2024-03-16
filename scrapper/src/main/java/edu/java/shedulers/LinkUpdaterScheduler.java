package edu.java.shedulers;

import edu.java.clients.BotClient;
import edu.java.clients.GithubClient;
import edu.java.clients.StackoverflowClient;
import edu.java.database.dto.Link;
import edu.java.parsers.WebHandler;
import edu.java.scrapperapi.services.LinkService;
import java.util.List;
import java.util.Map;
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
                System.out.println("need upd " + entry);
                WebHandler webHandler = handlerContainer.get(entry.getLink().getHost());
                webHandler.getUpdate(entry);
//                if (entry.getLink().getHost().equals("github.com")) {
//                    WebGithub webGithub = new WebGithub();
//                    GitResponseDto result = githubClient.getAnswersByQuestion(
//                        webGithub.getOwner(entry.getLink().getPath()),
//                        webGithub.getRepos(entry.getLink().getPath())
//                    ).collectList().block().getFirst();
//
//                    if (!entry.getLasthash().equals(result.nodeId)) {
//                        linkService.updateTimeAndLastHash(entry.getId(), result.nodeId);
//                        botClient.sendUpdate(
//                            entry.getId(),
//                            entry.getLink(),
//                            String.format(
//                                "commit\nMessage - %s\n,Created by: %s (%s)",
//                                result.commit.message,
//                                result.commit.committer.name,
//                                result.commit.committer.email
//                            ),
//                            linkService.getAllUsersWithLink(entry)
//                        );
//                    }
//                } else {
//                    WebStackoverflow webStackoverflow = new WebStackoverflow();
//                    ItemDto result = stackoverflowClient.getAnswersByQuestion(
//                        Integer.parseInt(webStackoverflow.getId(entry.getLink().getPath()))
//                    ).block().items.getFirst();
//
//                    if (!entry.getLasthash().equals(String.valueOf(result.questionId))) {
//                        linkService.updateTimeAndLastHash(entry.getId(), String.valueOf(result.questionId));
//                        botClient.sendUpdate(
//                            entry.getId(),
//                            entry.getLink(),
//                            String.format(
//                                "new answer\nFrom - %s",
//                                result.owner.displayName
//                            ),
//                            linkService.getAllUsersWithLink(entry)
//                        );
//                    }

//                }
            }
        );
    }

    private Map<String, WebHandler> getHandlerContainer() {
        return this.webHandlers.stream().collect(
            Collectors.toMap(WebHandler::getHost, e -> e)
        );
    }
}
