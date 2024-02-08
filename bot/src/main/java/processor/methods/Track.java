package processor.methods;

import database.Database;
import processor.MethodProcessor;
import static processor.Constants.FAIL_TRACK_MESSAGE;
import static processor.Constants.SUCCESSFUL_TRACK_MESSAGE;

public class Track implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, Database database) {
        if (param.length == 2) {
            database.addLink(chatId, param[1]);
            return SUCCESSFUL_TRACK_MESSAGE;
        } else {
            return FAIL_TRACK_MESSAGE;
        }
    }
}
