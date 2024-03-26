package edu.java.bot.botapi.services;

import edu.java.bot.dto.LinkUpdate;

public interface CommunicatorService {
    void sendMessage(LinkUpdate linkUpdate);
}
