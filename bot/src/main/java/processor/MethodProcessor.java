package processor;

import database.SimpleDatabase;

@FunctionalInterface public interface MethodProcessor {
    String get(String[] param, Long chatId, SimpleDatabase database);
}
