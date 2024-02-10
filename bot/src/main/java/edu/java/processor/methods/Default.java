package edu.java.processor.methods;

import edu.java.database.Database;
import edu.java.processor.MethodProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.processor.Constants.DEFAULT_MESSAGE;

public class Default implements MethodProcessor {

    @Override
    public String get(Update update, Database database) {
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
