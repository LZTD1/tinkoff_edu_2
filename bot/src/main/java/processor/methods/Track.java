package processor.methods;

import database.SimpleDatabase;
import processor.MethodProcessor;

public class Track implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, SimpleDatabase database) {
        if (param.length == 2) {
            database.addLink(chatId, param[1]);
            return "Ссылка успешно затрекана!";
        } else {
            return "Не верный форма ввода!\nНапишите: /track url";
        }
    }
}
