package edu.java.processor.processors;

import edu.java.processor.MethodProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.processor.Constants.DEFAULT_MESSAGE;

public class DefaultHandler implements MethodProcessor {

    @Override
    public String handle(Update update) {
        return DEFAULT_MESSAGE;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
