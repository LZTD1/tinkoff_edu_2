package edu.java.configuration.AccessTypes;

import edu.java.database.dto.User;
import edu.java.domain.jdbc.UsersDao;
import edu.java.domain.jpa.LinkRepository;
import edu.java.domain.jpa.UserLinkRelRepository;
import edu.java.domain.jpa.UserRepository;
import edu.java.scrapperapi.services.LinkService;
import edu.java.scrapperapi.services.TgChatService;
import edu.java.scrapperapi.services.jdbc.JdbcTgChatService;
import edu.java.scrapperapi.services.jpa.JpaLinkService;
import edu.java.scrapperapi.services.jpa.JpaTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public LinkService linkService(
        LinkRepository linkRepository,
        UserRepository userRepository,
        UserLinkRelRepository userLinkRelRepository
    ) {
        return new JpaLinkService(linkRepository, userRepository, userLinkRelRepository);
    }

    @Bean
    public TgChatService tgChatService(
        UserRepository userRepository
    ) {
        return new JpaTgChatService(userRepository);
    }

}
