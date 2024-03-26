package edu.java.bot.processor.processors;

import edu.java.bot.processor.MethodProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.DEFAULT_MESSAGE;

@Component
public class DefaultHandler implements MethodProcessor {

    @Override
    public String handle(Update update) {
        return DEFAULT_MESSAGE;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }
}
