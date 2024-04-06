package edu.java.scrapperapi.exceptions;

public class TooManyRequests extends RuntimeException {
    public TooManyRequests() {
        super("Too many requests. Try later, maybe after minute.");
    }
}
