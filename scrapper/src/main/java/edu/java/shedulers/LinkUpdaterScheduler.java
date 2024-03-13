package edu.java.shedulers;

import edu.java.bot.dto.LinkUpdate;
import edu.java.clients.GithubClient;
import edu.java.clients.StackoverflowClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.database.dto.Link;
import edu.java.parsers.MainHandler;
import edu.java.scrapperapi.services.LinkService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(prefix = "app.scheduler", name = "enable", havingValue = "true")
public class LinkUpdaterScheduler {

    private final static Logger LOGGER = LogManager.getLogger();
    private LinkService linkService;
    private ApplicationConfig applicationConfig;
    private GithubClient githubClient;
    private StackoverflowClient stackoverflowClient;

    @Autowired
    public LinkUpdaterScheduler(
        LinkService linkService,
        ApplicationConfig applicationConfig,
        GithubClient githubClient,
        StackoverflowClient stackoverflowClient
    ) {
        this.linkService = linkService;
        this.applicationConfig = applicationConfig;
        this.githubClient = githubClient;
        this.stackoverflowClient = stackoverflowClient;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        LOGGER.info("Updating...");
        List<Link> list = linkService.listScheduler(applicationConfig.linkDelay());
        list.forEach(
            entry -> {
                System.out.println("Need upd, " + entry.toString());
                LinkUpdate linkUpdate = MainHandler.handle(entry.getLink());
            }
        );
    }
}
