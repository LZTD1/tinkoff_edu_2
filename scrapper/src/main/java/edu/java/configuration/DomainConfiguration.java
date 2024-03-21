package edu.java.configuration;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DomainConfiguration {

    @Bean
    public JdbcTemplate template(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
