package edu.java.clients;

import edu.java.bot.dto.LinkUpdate;
import edu.java.kafka.messages.LinkUpdateOuterClass;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class KafkaClient implements ProducerClient {

    private final KafkaTemplate<String, LinkUpdateOuterClass.LinkUpdate> template;

    @Override
    public void sendUpdates(List<LinkUpdate> linkUpdateList) {
        linkUpdateList.forEach(entry -> sendUpdate(
            entry.getId(),
            entry.getUrl(),
            entry.getDescription(),
            entry.getTgChatIds()
        ));
    }

    @Override
    public void sendUpdate(Long id, URI url, String description, List<Long> tgChatIds) {
        LinkUpdateOuterClass.LinkUpdate link = LinkUpdateOuterClass.LinkUpdate.newBuilder()
            .setDescription(description)
            .setId(id)
            .setUrl(url.toString())
            .addAllTgChatIds(tgChatIds)
            .build();

        template.send("messages.protobuf", link);
    }
}
