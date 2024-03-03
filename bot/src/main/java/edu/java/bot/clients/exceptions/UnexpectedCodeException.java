package edu.java.bot.clients.exceptions;

public class UnexpectedCodeException extends RuntimeException {

    public UnexpectedCodeException(int code) {
        super("Неожиданный код: " + code);
    }

}
