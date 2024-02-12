package edu.java.processor.processors;

import edu.java.processor.MethodProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.processor.Constants.START_MESSAGE;

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
