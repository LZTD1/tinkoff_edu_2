package edu.java.configuration.AccessTypes;

import edu.java.domain.jdbc.LinksDao;
import edu.java.domain.jdbc.UserLinkRelationDao;
import edu.java.domain.jdbc.UsersDao;
import edu.java.scrapperapi.services.LinkService;
import edu.java.scrapperapi.services.TgChatService;
import edu.java.scrapperapi.services.jdbc.JdbcLinkService;
import edu.java.scrapperapi.services.jdbc.JdbcTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public LinkService linkService(
        LinksDao linksDao,
        UsersDao usersDao,
        UserLinkRelationDao userLinkRelationDao
    ) {
        return new JdbcLinkService(linksDao, usersDao, userLinkRelationDao);
    }

    @Bean
    public TgChatService tgChatService(
        UsersDao usersDao
    ) {
        return new JdbcTgChatService(usersDao);
    }

}
