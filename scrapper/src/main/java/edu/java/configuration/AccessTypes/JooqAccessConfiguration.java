package edu.java.configuration.AccessTypes;

import edu.java.domain.jooq.JooqUserRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaUserLinkRelRepository;
import edu.java.domain.jpa.JpaUserRepository;
import edu.java.scrapperapi.services.LinkService;
import edu.java.scrapperapi.services.TgChatService;
import edu.java.scrapperapi.services.jooq.JooqLinkService;
import edu.java.scrapperapi.services.jooq.JooqTgChatService;
import edu.java.scrapperapi.services.jpa.JpaLinkService;
import edu.java.scrapperapi.services.jpa.JpaTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {

    @Bean
    public LinkService linkService(

    ) {
        return new JooqLinkService();
    }

    @Bean
    public TgChatService tgChatService(
        JooqUserRepository jooqUserRepository
    ) {
        return new JooqTgChatService(jooqUserRepository);
    }

}
