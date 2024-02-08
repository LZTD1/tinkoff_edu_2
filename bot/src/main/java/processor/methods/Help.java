package processor.methods;

import database.SimpleDatabase;
import processor.MethodProcessor;

public class Help implements MethodProcessor {

    @Override
    public String get(String[] param, Long chatId, SimpleDatabase database) {
        return "/start -- зарегистрировать пользователя\n"
            + "/help -- вывести окно с командами\n"
            + "/track -- начать отслеживание ссылки\n"
            + "/untrack -- прекратить отслеживание ссылки\n"
            + "/list -- показать список отслеживаемых ссылок";
    }
}
