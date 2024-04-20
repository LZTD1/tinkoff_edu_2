package edu.java.configuration;

import edu.java.configuration.accessTypes.AccessType;
import edu.java.configuration.retryConfiguration.BackoffType;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,

    int linkDelay,
    int limitPerCheck,
    AccessType databaseAccessType,

    RetryableConfig retryableConfig,
    BucketConfig bucketConfig,
    boolean useQueue,
    KafkaConfiguration kafkaConfiguration,
    String botUrl
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record RetryableConfig(List<Integer> statusCodes, BackoffType backoffType) {
    }

    public record BucketConfig(Long tokens, Long refillTokens, Long millisOfRefill) {
    }

    public record KafkaConfiguration(
        String bootstrapServers,
        String topicName
    ) {
    }
}
