package edu.java.bot.processor.processors;

import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.clients.exceptions.ConflictError;
import edu.java.bot.processor.MethodProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.START_MESSAGE;

@Component
@RequiredArgsConstructor
public class StartHandler implements MethodProcessor {

    private final ScrapperClient scrapperClient;

    @Override
    public String handle(Update update) {
        try {
            scrapperClient.registerUser(update.getMessage().getChatId());
            return START_MESSAGE;
        } catch (ConflictError e) {
            return e.getMessage();
        }
    }

    @Override
    public String getDescription() {
        return "зарегистрировать пользователя";
    }

    @Override
    public String getName() {
        return "/start";
    }
}
