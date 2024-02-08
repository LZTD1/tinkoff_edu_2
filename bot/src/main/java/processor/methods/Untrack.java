package processor.methods;

import database.Database;
import processor.MethodProcessor;
import static processor.Constants.FAIL_UNTRACK_MESSAGE;
import static processor.Constants.SUCCESSFUL_UNTRACK_MESSAGE;

public class Untrack implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, Database database) {
        if (param.length == 2) {
            database.removeLink(chatId, param[1]);
            return SUCCESSFUL_UNTRACK_MESSAGE;
        } else {
            return FAIL_UNTRACK_MESSAGE;
        }
    }
}
