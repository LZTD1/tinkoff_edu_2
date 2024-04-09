package edu.java.bot.configuration.kafka;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import edu.java.bot.serdes.LinkUpdateDeserializer;
import edu.java.kafka.messages.LinkUpdateOuterClass;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import java.util.Map;

@Configuration
public class ProtobufKafkaConsumerConfiguration {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateOuterClass.LinkUpdate> protobufMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateOuterClass.LinkUpdate> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29091,localhost:29092,localhost:29093",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
            ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, LinkUpdateDeserializer.class.getName()
        )));

        return factory;
    }
}
