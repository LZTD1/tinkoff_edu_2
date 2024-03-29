package edu.java.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleDatabase implements Database {

    private final static Logger LOGGER = LogManager.getLogger();
    private static SimpleDatabase instance;
    private final Map<Long, List<String>> database;

    private SimpleDatabase() {
        this.database = new HashMap();
    }

    public static SimpleDatabase getInstance() {
        if (instance == null) {
            synchronized (SimpleDatabase.class) {
                if (instance == null) {
                    instance = new SimpleDatabase();
                }
            }
        }
        return instance;
    }

    @Override
    public void registerUser(Long userId) {
        database.put(userId, new ArrayList<>());
        LOGGER.info("Пользователь {}, зарегистрировался", userId);
    }

    @Override
    public List<String> getUserLinksById(Long userId) {
        return database.getOrDefault(userId, new ArrayList<String>());
    }

    @Override
    public void addLink(Long userId, String url) {
        List<String> userLinks = database.getOrDefault(userId, new ArrayList<String>());
        userLinks.add(url);
        database.put(userId, userLinks);
        LOGGER.info("Пользователь {}, добавил ссылку - {}", userId, url);
    }

    @Override
    public void removeLink(Long userId, String url) {
        List<String> user = database.getOrDefault(userId, new ArrayList<String>());
        user.remove(url);
        database.put(userId, user);
        LOGGER.info("Пользователь {}, удалил ссылку - {}", userId, url);
    }

    @Override
    public boolean isUserRegister(Long userId) {
        return database.containsKey(userId);
    }

    public void dropAll() {
        instance.database.clear();
    }
}
