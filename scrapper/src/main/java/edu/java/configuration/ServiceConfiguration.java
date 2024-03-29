package edu.java.configuration;

import edu.java.domain.jdbc.LinksDao;
import edu.java.domain.jdbc.UserLinkRelationDao;
import edu.java.domain.jdbc.UsersDao;
import edu.java.scrapperapi.services.LinkService;
import edu.java.scrapperapi.services.TgChatService;
import edu.java.scrapperapi.services.jdbc.JdbcLinkService;
import edu.java.scrapperapi.services.jdbc.JdbcTgChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    private LinksDao linksDao;
    private UsersDao usersDao;
    private UserLinkRelationDao userLinkRelationDao;

    public ServiceConfiguration(LinksDao linksDao, UsersDao usersDao, UserLinkRelationDao userLinkRelationDao) {
        this.linksDao = linksDao;
        this.usersDao = usersDao;
        this.userLinkRelationDao = userLinkRelationDao;
    }

    @Bean
    public LinkService getLinkService() {
        return new JdbcLinkService(
            linksDao, usersDao, userLinkRelationDao
        );
    }

    @Bean
    public TgChatService getTgChatService() {
        return new JdbcTgChatService(
            usersDao
        );
    }
}
