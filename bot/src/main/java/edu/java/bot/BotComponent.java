package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.processor.MethodProcessor;
import edu.java.bot.processor.ProcessorHolder;
import edu.java.bot.processor.processors.DefaultHandler;
import edu.java.bot.telegramExceptions.RuntimeTelegramApiRequestException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Component
@RequiredArgsConstructor
public class BotComponent extends TelegramLongPollingBot {

    private final static Logger LOGGER = LogManager.getLogger();
    private final ApplicationConfig config;
    private final ProcessorHolder processorHolder;
    private final RetryTemplate retry;
    private final MeterRegistry meterRegistry;
    private Counter receivedMessages;
    private Counter processedMessages;

    @PostConstruct
    private void init() {
        receivedMessages = meterRegistry.counter("receivedMessages");
        processedMessages = meterRegistry.counter("processedMessages");
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
            receivedMessages.increment();

            MethodProcessor processor = processorHolder.getCommandByName(messageText.split(" ")[0]);
            sendMessage(chatId, processor.handle(update), Parsemode.HTML);
        }
    }

    private void setCommands() {
        List<BotCommand> list = processorHolder.getAllCommands()
            .stream()
            .filter(entry -> !entry.getClass().equals(DefaultHandler.class))
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
        sendMessage(chatId, text, Parsemode.MARKDOWN);
    }

    public void sendMessage(long chatId, String text, Parsemode parseMode) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        message.setParseMode(parseMode.getString());
        message.disableWebPagePreview();

        retry.execute(context -> {
            warnIfRetry(chatId, context);

            try {
                execute(message);
            } catch (TelegramApiRequestException e) {
                throw new RuntimeTelegramApiRequestException(e);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            processedMessages.increment();
            return null;
        });
    }

    private void warnIfRetry(Long chatId, RetryContext context) {
        if (context.getRetryCount() > 0) {
            LOGGER.warn(
                "[TG] Повторная попытка отправки сообщения юзеру - {} ({} попытка)",
                chatId,
                context.getRetryCount() + 1
            );
        }
    }
}
