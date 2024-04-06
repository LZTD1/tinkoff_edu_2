package edu.java.bot.configuration.retryConfiguration.BackoffPolicies;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "app.retryable-config", name = "backoff-type", havingValue = "constant")
public class Constant extends FixedBackOffPolicy {
}
