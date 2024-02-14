package edu.java.processor.processors;

import edu.java.database.Database;
import edu.java.processor.MethodProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.database.SimpleDatabase.getInstance;
import static edu.java.processor.Constants.FAIL_UNTRACK_MESSAGE;
import static edu.java.processor.Constants.SUCCESSFUL_UNTRACK_MESSAGE;

public class UntrackHandler implements MethodProcessor {

    private final Database database;

    public UntrackHandler() {
        this.database = getInstance();
    }

    @Override
    public String handle(Update update) {
        String[] param = update.getMessage().getText().split(" ");
        if (param.length == 2) {
            this.database.removeLink(update.getMessage().getChatId(), param[1]);
            return SUCCESSFUL_UNTRACK_MESSAGE;
        } else {
            return FAIL_UNTRACK_MESSAGE;
        }
    }

    @Override
    public String getDescription() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    public String getName() {
        return "/untrack";
    }
}
