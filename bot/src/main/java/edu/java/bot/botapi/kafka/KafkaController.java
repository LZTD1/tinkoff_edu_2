package edu.java.bot.botapi.kafka;

import edu.java.bot.botapi.services.CommunicatorService;
import edu.java.kafka.messages.LinkUpdateOuterClass;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import static edu.java.bot.botapi.map.LinkUpdateOuterMap.mapFromOuter;

@Component
@RequiredArgsConstructor
public class KafkaController {

    private final CommunicatorService communicatorService;

    private final static Logger LOGGER = LogManager.getLogger();

    @KafkaListener(topics = "messages.protobuf",
                   groupId = "messages-group",
                   containerFactory = "protobufMessageKafkaListenerContainerFactory")
    public void listenMessages(LinkUpdateOuterClass.LinkUpdate linkUpdate) {
        communicatorService.sendMessage(mapFromOuter(linkUpdate));
    }
}
