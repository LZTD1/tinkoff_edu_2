package edu.java.bot.telegramExceptions;

import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class RuntimeTelegramApiRequestException extends RuntimeException {
    private final TelegramApiRequestException e;

    public RuntimeTelegramApiRequestException(TelegramApiRequestException e) {
        this.e = e;
    }

    public int getStatusCode() {
        return e.getErrorCode();
    }
}
