package edu.java.scrapperapi.exceptions;

public class LinkIsNotValidException extends RuntimeException {
    public LinkIsNotValidException(Exception e) {
        super(e);
    }
}
