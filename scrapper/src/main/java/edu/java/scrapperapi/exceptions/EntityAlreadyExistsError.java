package edu.java.scrapperapi.exceptions;

public class EntityAlreadyExistsError extends RuntimeException {
    public EntityAlreadyExistsError(String s) {
        super(s);
    }
}
