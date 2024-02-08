package processor;

import database.Database;

@FunctionalInterface public interface MethodProcessor {
    String get(String[] param, Long chatId, Database database);
}
