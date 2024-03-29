package edu.java.bot.processor.processors;

import edu.java.bot.processor.MethodProcessor;
import edu.java.database.Database;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.EMPTY_LIST_MESSAGE;
import static edu.java.database.SimpleDatabase.getInstance;

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
