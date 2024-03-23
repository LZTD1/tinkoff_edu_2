package edu.java.scrapperapi.services;

public interface TgChatService {
    void register(long tgChatId);

    void unregister(long tgChatId);
}
