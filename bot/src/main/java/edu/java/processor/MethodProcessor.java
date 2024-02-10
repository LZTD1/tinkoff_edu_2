package edu.java.processor;

import edu.java.database.Database;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MethodProcessor {
    String get(Update update, Database database);

    String getDescription();

    String getName();
}
