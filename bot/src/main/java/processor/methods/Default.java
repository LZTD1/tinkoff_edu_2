package processor.methods;

import database.SimpleDatabase;
import processor.MethodProcessor;

public class Default implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, SimpleDatabase database) {
        return "Упс, какая-то не ожиданная команда!";
    }
}
