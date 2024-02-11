package edu.java.processor;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MethodProcessor {
    String get(Update update);

    String getDescription();

    String getName();
}