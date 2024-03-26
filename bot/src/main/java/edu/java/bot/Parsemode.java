package edu.java.bot;

public enum Parsemode {
    HTML("html"),
    MARKDOWN("markdown");

    private final String parsemode;

    Parsemode(String parsemode) {
        this.parsemode = parsemode;
    }

    public String getString() {
        return parsemode;
    }
}
