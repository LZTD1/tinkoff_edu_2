package processor.methods;

import database.Database;
import processor.MethodProcessor;
import static processor.Constants.EMPTY_LIST_MESSAGE;

public class List implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, Database database) {
        var links = database.getUserLinksById(chatId);
        if (links.isEmpty()) {
            return EMPTY_LIST_MESSAGE;
        }
        return String.join("\n", links);
    }
}
