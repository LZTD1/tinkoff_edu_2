package edu.java.bot;

import database.SimpleDatabase;
import edu.java.bot.configuration.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import static processor.Processor.getAnswer;

@Component
public class BotComponent extends TelegramLongPollingBot {

    private final ApplicationConfig config;
    private final SimpleDatabase database;

    @SuppressWarnings("LineLength")
    public BotComponent(ApplicationConfig applicationConfig) {
        this.config = applicationConfig;
        this.database =
            new SimpleDatabase(); // Не могу понять, как не создавать базу тут, а протянуть на прямую в методы использующие ее
    }

    @Override
    public String getBotUsername() {
        return config.telegramName();
    }

    @Override
    public String getBotToken() {
        return "6375608618:AAEKmvE9RmWpGKY2L0Qam7CLTjvQOXfuZ1w";
//        return config.telegramToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            sendMessage(
                chatId,
                getAnswer(messageText, chatId, database)
            ); // вот тут колхозно приходиться ее опрокидывать в методы
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
