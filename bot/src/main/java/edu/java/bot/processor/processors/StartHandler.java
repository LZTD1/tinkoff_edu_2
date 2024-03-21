package edu.java.bot.processor.processors;

import edu.java.bot.processor.MethodProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.START_MESSAGE;

@Component
public class StartHandler implements MethodProcessor {
    @Override
    public String handle(Update update) {
        return START_MESSAGE;
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
