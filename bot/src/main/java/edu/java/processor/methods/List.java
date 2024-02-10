package edu.java.processor.methods;

import edu.java.database.Database;
import edu.java.processor.MethodProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.processor.Constants.EMPTY_LIST_MESSAGE;

public class List implements MethodProcessor {

    @Override
    public String get(Update update, Database database) {
        var links = database.getUserLinksById(update.getMessage().getChatId());
        if (links.isEmpty()) {
            return EMPTY_LIST_MESSAGE;
        }
        return String.join("\n", links);
    }

    @Override
    public String getDescription() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    public String getName() {
        return "/list";
    }
}
