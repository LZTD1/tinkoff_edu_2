package edu.java.configuration;

import edu.java.clients.GithubClient;
import edu.java.clients.StackoverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public GithubClient githubClient() {
        return new GithubClient();
    }

    @Bean
    public StackoverflowClient stackoverflowClient() {
        return new StackoverflowClient();
    }
}
