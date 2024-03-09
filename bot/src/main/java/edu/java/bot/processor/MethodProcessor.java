package edu.java.bot.processor;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MethodProcessor {
    String handle(Update update);

    String getDescription();

    String getName();
}
