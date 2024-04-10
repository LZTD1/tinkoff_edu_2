package edu.java.scrapper.kafkaTest;

import edu.java.clients.KafkaClient;
import edu.java.configuration.kafka.ProtobufKafkaProducerConfiguration;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class TestKafkaProducer {

    @Container
    public static KafkaContainer container = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));
    private static KafkaClient kafkaClient;
    private static Properties consumerProps;

    @BeforeAll
    static void setUpKafkaClient() {
        var producer = new ProtobufKafkaProducerConfiguration().protobufMessageKafkaTemplate(
            container.getHost() + ":" + container.getFirstMappedPort());
        kafkaClient = new KafkaClient(producer);
    }

    @BeforeAll
    static void setUpConsumerProps() {
        consumerProps = new Properties();
        consumerProps.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, container.getBootstrapServers());
        consumerProps.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "messages-group");
        consumerProps.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.setProperty(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            ByteArrayDeserializer.class.getName()
        );
        consumerProps.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    @SneakyThrows
    @Test
    void testSendUpdate() {

        kafkaClient.sendUpdate(
            1L,
            URI.create("vk.com"),
            "desc",
            List.of()
        );
        Thread.sleep(500);

        try (KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(Collections.singletonList("messages.protobuf"));
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(1000));

            assertEquals(1, records.count());
        }
    }
}
