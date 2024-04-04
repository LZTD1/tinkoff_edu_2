package edu.java.configuration;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.local.LocalBucketBuilder;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfiguration {

    @Value("${app.bucket-config.tokens}")
    private Long tokens;

    @Value("${app.bucket-config.refill-tokens}")
    private Long refillTokens;

    @Value("${app.bucket-config.millis-of-refill}")
    private Long duration;

    @Bean
    public LocalBucketBuilder getBucket() {
        return Bucket.builder()
            .addLimit(limit -> limit
                .capacity(tokens)
                .refillGreedy(refillTokens, Duration.ofMillis(duration)));
    }
}
