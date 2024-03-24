package edu.java.configuration;

import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcUserLinkRelRepository;
import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.scrapperapi.services.LinkService;
import edu.java.scrapperapi.services.TgChatService;
import edu.java.scrapperapi.services.jdbc.JdbcLinkService;
import edu.java.scrapperapi.services.jdbc.JdbcTgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ServiceConfiguration {

    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcUserRepository jdbcUserRepository;
    private final JdbcUserLinkRelRepository jdbcUserLinkRelRepository;

    @Bean
    public LinkService getLinkService() {
        return new JdbcLinkService(
            jdbcLinkRepository, jdbcUserRepository, jdbcUserLinkRelRepository
        );
    }

    @Bean
    public TgChatService getTgChatService() {
        return new JdbcTgChatService(
            jdbcUserRepository
        );
    }
}
