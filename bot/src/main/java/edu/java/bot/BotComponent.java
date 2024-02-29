package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.processor.MethodProcessor;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import static edu.java.bot.processor.ProcessorHolder.getAllCommands;
import static edu.java.bot.processor.ProcessorHolder.getCommandByName;

@Component
public class BotComponent extends TelegramLongPollingBot {

    private final static Logger LOGGER = LogManager.getLogger();
    private final ApplicationConfig config;

    public BotComponent(ApplicationConfig applicationConfig) {
        this.config = applicationConfig;
    }

    @Override
    public String getBotUsername() {
        setCommands();
        return config.telegramName();
    }

    @Override
    public String getBotToken() {
        return config.telegramToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            LOGGER.info("Пользователь {}, отправил - {}", chatId, messageText);

            MethodProcessor processor = getCommandByName(messageText.split(" ")[0]);

            sendMessage(chatId, processor.handle(update));
        }
    }

    private void setCommands() {
        List<BotCommand> list = getAllCommands()
            .stream()
            .map(entry -> new BotCommand(entry.getName(), entry.getDescription()))
            .toList();

        SetMyCommands commandsList = new SetMyCommands();
        commandsList.setCommands(list);

        try {
            execute(commandsList);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(long chatId, String text) {

        SendMessage message = new SendMessage(String.valueOf(chatId), text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
