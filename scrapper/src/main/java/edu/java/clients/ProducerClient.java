package edu.java.clients;

import edu.java.bot.dto.LinkUpdate;
import java.net.URI;
import java.util.List;

public interface ProducerClient {
    void sendUpdates(List<LinkUpdate> linkUpdateList);

    void sendUpdate(Long id, URI url, String description, List<Long> tgChatIds);
}
