package processor.methods;

import database.SimpleDatabase;
import processor.MethodProcessor;

public class Untrack implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, SimpleDatabase database) {
        if (param.length == 2) {
            database.removeLink(chatId, param[1]);
            return "Ссылка успешно откреплена!";
        } else {
            return "Не верный форма ввода!\nНапишите: /untrack url";
        }
    }
}
