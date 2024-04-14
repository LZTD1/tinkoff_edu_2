package edu.java.bot.botapi.kafka;

import edu.java.bot.botapi.services.CommunicatorService;
import edu.java.kafka.messages.LinkUpdateOuterClass;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import static edu.java.bot.botapi.map.LinkUpdateOuterMap.mapFromOuter;

@Component
@RequiredArgsConstructor
public class KafkaController {

    private final static Logger LOGGER = LogManager.getLogger();
    private final CommunicatorService communicatorService;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final KafkaDlq kafkaDlq;

    @KafkaListener(
        topics = "${app.kafka-configuration.topic-name}",
        groupId = "${app.kafka-configuration.group-id}",
        containerFactory = "protobufMessageKafkaListenerContainerFactory"
    )
    public void listenMessages(
        LinkUpdateOuterClass.LinkUpdate linkUpdate,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        try {
            communicatorService.sendMessage(mapFromOuter(linkUpdate));
            LOGGER.info("[KAFKA] Получено сообщение в {} топик, {} партиции", topic, partition);
        } catch (Exception e) {
            kafkaDlq.send(linkUpdate, kafkaTemplate);
        }
    }
}
