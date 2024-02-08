package processor.methods;

import database.SimpleDatabase;
import processor.MethodProcessor;

public class Start implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, SimpleDatabase database) {
        return "Привет";
    }
}
