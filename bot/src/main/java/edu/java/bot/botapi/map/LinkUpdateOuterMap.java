package edu.java.bot.botapi.map;

import edu.java.bot.dto.LinkUpdate;
import edu.java.kafka.messages.LinkUpdateOuterClass;
import java.net.URI;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkUpdateOuterMap {
    public static LinkUpdate mapFromOuter(LinkUpdateOuterClass.LinkUpdate linkUpdate) {
        return new LinkUpdate() {{
            setId(linkUpdate.getId());
            setDescription(linkUpdate.getDescription());
            setUrl(URI.create(linkUpdate.getUrl()));
            setTgChatIds(linkUpdate.getTgChatIdsList());
        }};
    }
}
