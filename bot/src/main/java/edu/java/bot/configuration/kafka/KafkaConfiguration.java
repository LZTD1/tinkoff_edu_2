package edu.java.bot.configuration.kafka;

import edu.java.bot.botapi.kafka.KafkaDlq;
import edu.java.bot.serdes.LinkUpdateDeserializer;
import edu.java.kafka.messages.LinkUpdateOuterClass;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    @Value("${app.kafka-configuration.dlq-configuration.interval-between-attempts}")
    private long interval;

    @Value("${app.kafka-configuration.dlq-configuration.max-attempts-before-dlq}")
    private long maxAttempts;

    private final KafkaDlq kafkaDlq;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateOuterClass.LinkUpdate>
    protobufMessageKafkaListenerContainerFactory(
        @Value("${app.kafka-configuration.bootstrap-servers}") String bootstrapServers
    ) {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateOuterClass.LinkUpdate> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
            ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, LinkUpdateDeserializer.class.getName()
        )));

        DeadLetterPublishingRecoverer dlqPublisher = new DeadLetterPublishingRecoverer(
            retryableTopicKafkaTemplate(bootstrapServers),
            kafkaDlq.getFunctionByDlq()
        );

        factory.setCommonErrorHandler(new DefaultErrorHandler(
            dlqPublisher,
            new FixedBackOff(interval, maxAttempts)
        ));

        return factory;
    }

    @Bean
    public KafkaTemplate<String, byte[]> retryableTopicKafkaTemplate(
        @Value("${app.kafka-configuration.bootstrap-servers}") String bootstrapServers
    ) {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class
        )));
    }
}
