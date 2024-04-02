package edu.java.configuration.retryConfiguration.BackoffPolicies;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "app.retryable-config", name = "backoff-type", havingValue = "linear")
public class Linear extends ExponentialBackOffPolicy {
}
