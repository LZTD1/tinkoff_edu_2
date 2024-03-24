package edu.java.configuration;

import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcUserLinkRelRepository;
import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.scrapperapi.services.LinkService;
import edu.java.scrapperapi.services.TgChatService;
import edu.java.scrapperapi.services.jdbc.JdbcLinkService;
import edu.java.scrapperapi.services.jdbc.JdbcTgChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    private JdbcLinkRepository jdbcLinkRepository;
    private JdbcUserRepository jdbcUserRepository;
    private JdbcUserLinkRelRepository jdbcUserLinkRelRepository;

    public ServiceConfiguration(JdbcLinkRepository jdbcLinkRepository, JdbcUserRepository jdbcUserRepository, JdbcUserLinkRelRepository jdbcUserLinkRelRepository) {
        this.jdbcLinkRepository = jdbcLinkRepository;
        this.jdbcUserRepository = jdbcUserRepository;
        this.jdbcUserLinkRelRepository = jdbcUserLinkRelRepository;
    }

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
