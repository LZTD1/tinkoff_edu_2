package edu.java.processor.processors;

import edu.java.database.Database;
import edu.java.processor.MethodProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.database.SimpleDatabase.getInstance;
import static edu.java.processor.Constants.EMPTY_LIST_MESSAGE;

public class ListHandler implements MethodProcessor {

    private final Database database;

    public ListHandler() {
        this.database = getInstance();
    }

    @Override
    public String handle(Update update) {
        var links = this.database.getUserLinksById(update.getMessage().getChatId());
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
