package edu.java.bot.configuration.retryConfiguration.BackoffPolicies;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.backoff.ExponentialRandomBackOffPolicy;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "app.retryable-config", name = "backoff-type", havingValue = "exponent")
public class Exponent extends ExponentialRandomBackOffPolicy {
}
