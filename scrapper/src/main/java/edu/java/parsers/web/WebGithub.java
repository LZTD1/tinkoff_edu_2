package edu.java.parsers.web;

public class WebGithub {

    public String getRepos(String path) {
        return path.split("/")[2];
    }

    public String getOwner(String path) {
        return path.split("/")[1];
    }

}
