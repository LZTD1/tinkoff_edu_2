package edu.java.processor.methods;

import edu.java.database.Database;
import edu.java.processor.MethodProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.processor.Constants.START_MESSAGE;

public class Start implements MethodProcessor {

    @Override
    public String get(Update update, Database database) {
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
