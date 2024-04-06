package edu.java.configuration.accessTypes;

import edu.java.domain.jooq.JooqLinkRepository;
import edu.java.domain.jooq.JooqUserLinkRelRepository;
import edu.java.domain.jooq.JooqUserRepository;
import edu.java.scrapperapi.services.LinkService;
import edu.java.scrapperapi.services.TgChatService;
import edu.java.scrapperapi.services.jooq.JooqLinkService;
import edu.java.scrapperapi.services.jooq.JooqTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {

    @Bean
    public LinkService linkService(
        JooqUserRepository jooqUserRepository,
        JooqUserLinkRelRepository jooqUserLinkRelRepository,
        JooqLinkRepository jooqLinkRepository
    ) {
        return new JooqLinkService(
            jooqLinkRepository,
            jooqUserLinkRelRepository,
            jooqUserRepository
        );
    }

    @Bean
    public TgChatService tgChatService(
        JooqUserRepository jooqUserRepository
    ) {
        return new JooqTgChatService(jooqUserRepository);
    }

}
