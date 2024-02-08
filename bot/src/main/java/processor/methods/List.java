package processor.methods;

import database.SimpleDatabase;
import processor.MethodProcessor;

public class List implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, SimpleDatabase database) {
        var links = database.getUserLinksById(chatId);
        if (links.isEmpty()) {
            return "У вас нету никаких ссылок!";
        }
        return String.join("\n", links);
    }
}
