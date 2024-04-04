package edu.java.scrapperapi;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.local.LocalBucketBuilder;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateLimiterByIp {
    private final Map<String, Bucket> buckets = new HashMap<>();
    private final LocalBucketBuilder configurationBuilder;

    public boolean tryConsume(String ipAddr) {
        return buckets
            .computeIfAbsent(ipAddr, ip -> configurationBuilder.build())
            .tryConsume(1);
    }
}
