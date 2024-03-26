package edu.java.configuration.AccessTypes;

import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaUserLinkRelRepository;
import edu.java.domain.jpa.JpaUserRepository;
import edu.java.scrapperapi.services.LinkService;
import edu.java.scrapperapi.services.TgChatService;
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
        JpaLinkRepository jpaLinkRepository,
        JpaUserRepository jpaUserRepository,
        JpaUserLinkRelRepository jpaUserLinkRelRepository
    ) {
        return new JpaLinkService(jpaLinkRepository, jpaUserRepository, jpaUserLinkRelRepository);
    }

    @Bean
    public TgChatService tgChatService(
        JpaUserRepository jpaUserRepository
    ) {
        return new JpaTgChatService(jpaUserRepository);
    }

}
