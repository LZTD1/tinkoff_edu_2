package edu.java.configuration.AccessTypes;

import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcUserLinkRelRepository;
import edu.java.domain.jdbc.JdbcUserRepository;
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
        JdbcLinkRepository jdbcLinkRepository,
        JdbcUserRepository jdbcUserRepository,
        JdbcUserLinkRelRepository jdbcUserLinkRelRepository
    ) {
        return new JdbcLinkService(jdbcLinkRepository, jdbcUserRepository, jdbcUserLinkRelRepository);
    }

    @Bean
    public TgChatService tgChatService(
        JdbcUserRepository jdbcUserRepository
    ) {
        return new JdbcTgChatService(jdbcUserRepository);
    }

}
