package edu.java.parsers.web;

public class WebStackoverflow {

    public String getHost() {
        return "stackoverflow.com";
    }

    public String getId(String path) {
        return path.split("/")[2];
    }
}
