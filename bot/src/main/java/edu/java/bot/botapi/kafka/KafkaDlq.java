package edu.java.bot.botapi.kafka;

import edu.java.kafka.messages.LinkUpdateOuterClass;
import java.util.function.BiFunction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaDlq {
    private final static Logger LOGGER = LogManager.getLogger();

    public static void send(LinkUpdateOuterClass.LinkUpdate linkUpdate, KafkaTemplate<String, byte[]> kafkaTemplate) {
        LOGGER.warn("[KAFKA] Ошибка обработки! Сообщение улетело в dlq");
        kafkaTemplate.send("messages.protobuf-dlq", linkUpdate.toByteArray());
    }

    public static BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition>
    getFunctionByDlq() {
        return (r, e) -> {
            LOGGER.warn("[KAFKA] Невалидное сообщение в топике! Отослано в dlq");
            return new TopicPartition("messages.protobuf-dlq", r.partition());
        };
    }

}
