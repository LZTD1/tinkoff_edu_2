package edu.java.bot.configuration;

import edu.java.bot.configuration.retryConfiguration.BackoffType;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotEmpty
    String telegramName,
    RetryableConfig retryableConfig
) {
    public record RetryableConfig(List<Integer> statusCodes, BackoffType backoffType) {
    }
}
