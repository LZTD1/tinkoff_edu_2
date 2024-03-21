package edu.java.bot.botapi.services;

import edu.java.bot.BotComponent;
import edu.java.bot.dto.LinkUpdate;
import org.springframework.stereotype.Service;

@Service
public class BotCommunicatorService implements CommunicatorService {

    private BotComponent botComponent;

    public BotCommunicatorService(BotComponent botComponent) {
        this.botComponent = botComponent;
    }

    @Override
    public void sendMessage(LinkUpdate linkUpdate) {
        linkUpdate.getTgChatIds().forEach(tgId -> botComponent.sendMessage(
            tgId, linkUpdate.getDescription()
        ));
    }
}
