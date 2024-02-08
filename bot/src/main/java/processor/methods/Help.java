package processor.methods;

import database.Database;
import processor.MethodProcessor;
import static processor.Constants.HELP_MESSAGE;

public class Help implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, Database database) {
        return HELP_MESSAGE;
    }
}
