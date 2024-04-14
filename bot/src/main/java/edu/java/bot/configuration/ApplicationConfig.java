package edu.java.bot.configuration;

import edu.java.bot.configuration.retryConfiguration.BackoffType;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotEmpty
    String telegramName,
    RetryableConfig retryableConfig,
    KafkaConfiguration kafkaConfiguration
) {
    public record RetryableConfig(List<Integer> statusCodes, BackoffType backoffType) {
    }
    public record KafkaConfiguration(
        String bootstrapServers,
        String topicName,
        String groupId,
        DlqConfiguration dlqConfiguration
    ) {
        public record DlqConfiguration(
            String name,
            int maxAttemptsBeforeDlq,
            Long intervalBetweenAttempts
        ){}
    }
}
