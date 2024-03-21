package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.processor.MethodProcessor;
import java.util.List;
import edu.java.bot.processor.ProcessorHolder;
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


@Component
public class BotComponent extends TelegramLongPollingBot {

    private final static Logger LOGGER = LogManager.getLogger();
    private final ApplicationConfig config;
    private ProcessorHolder processorHolder;

    public BotComponent(ApplicationConfig applicationConfig, ProcessorHolder processorHolder) {
        this.config = applicationConfig;
        this.processorHolder = processorHolder;
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

            MethodProcessor processor = processorHolder.getCommandByName(messageText.split(" ")[0]);
            sendMessage(chatId, processor.handle(update));
        }
    }

    private void setCommands() {
        List<BotCommand> list = processorHolder.getAllCommands()
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

    public void sendMessage(long chatId, String text) {

        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        message.setParseMode("markdown");
        message.disableWebPagePreview();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
