package edu.java.bot.configuration;

import edu.java.bot.configuration.retryConfiguration.CustomRetryPolicy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {

    private final BackOffPolicy backOffPolicy;
    @Value("${app.retryable-config.status-codes}")
    private List<Integer> retryableStatusCodes;

    @Bean
    public RetryTemplate retryTemplate() {
        CustomRetryPolicy retryPolicy = new CustomRetryPolicy(retryableStatusCodes);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);

        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }
}
