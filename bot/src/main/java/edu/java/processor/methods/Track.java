package edu.java.processor.methods;

import edu.java.processor.MethodProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.database.SimpleDatabase.getInstance;
import static edu.java.processor.Constants.FAIL_TRACK_MESSAGE;
import static edu.java.processor.Constants.SUCCESSFUL_TRACK_MESSAGE;

public class Track implements MethodProcessor {

    @Override
    public String get(Update update) {
        var database = getInstance();
        String[] param = update.getMessage().getText().split(" ");
        if (param.length == 2) {
            database.addLink(update.getMessage().getChatId(), param[1]);
            return SUCCESSFUL_TRACK_MESSAGE;
        } else {
            return FAIL_TRACK_MESSAGE;
        }
    }

    @Override
    public String getDescription() {
        return "начать отслеживание ссылки";
    }

    @Override
    public String getName() {
        return "/track";
    }
}