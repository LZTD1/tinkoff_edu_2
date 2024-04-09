package edu.java.configuration.kafka;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import edu.java.kafka.messages.LinkUpdateOuterClass;
import edu.java.serdes.MessageSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Map;

@Configuration
public class ProtobufKafkaProducerConfiguration {

    @Bean
    public KafkaTemplate<String, LinkUpdateOuterClass.LinkUpdate> protobufMessageKafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29091,localhost:29092,localhost:29093",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class
        )));
    }
}
