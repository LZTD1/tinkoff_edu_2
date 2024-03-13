package edu.java.configuration;

import edu.java.domain.LinksDao;
import edu.java.domain.UserLinkRelationDao;
import edu.java.domain.UsersDao;
import edu.java.scrapperapi.services.LinkService;
import edu.java.scrapperapi.services.TgChatService;
import edu.java.scrapperapi.services.jdbc.JdbcLinkService;
import edu.java.scrapperapi.services.jdbc.JdbcTgChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ServiceConfiguration {

    private LinksDao linksDao;
    private UsersDao usersDao;
    private UserLinkRelationDao userLinkRelationDao;

    @Autowired
    public ServiceConfiguration(LinksDao linksDao, UsersDao usersDao, UserLinkRelationDao userLinkRelationDao) {
        this.linksDao = linksDao;
        this.usersDao = usersDao;
        this.userLinkRelationDao = userLinkRelationDao;
    }

    @Bean
    @Primary
    public LinkService getLinkService() {
        return new JdbcLinkService(
            linksDao, usersDao, userLinkRelationDao
        );
    }

    @Bean
    @Primary
    public TgChatService getTgChatService() {
        return new JdbcTgChatService(
            usersDao
        );
    }
}