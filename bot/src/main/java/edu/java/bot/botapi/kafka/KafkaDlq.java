package edu.java.bot.botapi.kafka;

import edu.java.kafka.messages.LinkUpdateOuterClass;
import java.util.function.BiFunction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaDlq {

    private final static Logger LOGGER = LogManager.getLogger();

    @Value("${app.kafka-configuration.dlq-configuration.name}")
    private String dlqName;

    public void send(LinkUpdateOuterClass.LinkUpdate linkUpdate, KafkaTemplate<String, byte[]> kafkaTemplate) {
        LOGGER.warn("[KAFKA] Ошибка обработки! Сообщение улетело в {}", dlqName);
        kafkaTemplate.send(dlqName, linkUpdate.toByteArray());
    }

    public BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition>
    getFunctionByDlq() {
        return (r, e) -> {
            LOGGER.warn("[KAFKA] Невалидное сообщение в топике! Отослано в {}", dlqName);
            return new TopicPartition(dlqName, r.partition());
        };
    }

}
