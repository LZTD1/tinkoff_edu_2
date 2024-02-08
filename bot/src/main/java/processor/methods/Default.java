package processor.methods;

import database.Database;
import processor.MethodProcessor;
import static processor.Constants.DEFAULT_MESSAGE;

public class Default implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, Database database) {
        return DEFAULT_MESSAGE;
    }
}
