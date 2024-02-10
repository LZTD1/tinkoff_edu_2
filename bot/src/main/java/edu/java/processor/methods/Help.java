package edu.java.processor.methods;

import edu.java.database.Database;
import edu.java.processor.Constants;
import edu.java.processor.MethodProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Help implements MethodProcessor {

    @Override
    public String get(Update update, Database database) {
        return Constants.HELP_MESSAGE;
    }

    @Override
    public String getDescription() {
        return "вывести окно с командами";
    }

    @Override
    public String getName() {
        return "/help";
    }
}
