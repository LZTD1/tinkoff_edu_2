package processor.methods;

import database.Database;
import processor.MethodProcessor;
import static processor.Constants.START_MESSAGE;

public class Start implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, Database database) {
        return START_MESSAGE;
    }
}
