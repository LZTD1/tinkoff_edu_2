package edu.java.database;

import java.util.List;

public interface Database {

    void registerUser(Long userId);

    List<String> getUserLinksById(Long userId);

    void addLink(Long userId, String url);

    void removeLink(Long userId, String url);

    boolean isUserRegister(Long userId);
}
