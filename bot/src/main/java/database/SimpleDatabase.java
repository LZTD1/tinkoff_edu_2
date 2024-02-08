package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SimpleDatabase implements Database {

    private final Map<Long, List<String>> database;

    public SimpleDatabase() {
        this.database = new HashMap();
    }

    @Override
    public void registerUser(Long userId) {
        database.put(userId, List.of());
    }

    @Override
    public List<String> getUserLinksById(Long userId) {
        return database.getOrDefault(userId, new ArrayList<String>());
    }

    @Override
    public void addLink(Long userId, String url) {
        List<String> user = database.getOrDefault(userId, new ArrayList<String>());
        user.add(url);
        database.put(userId, user);
    }

    @Override
    public void removeLink(Long userId, String url) {
        List<String> user = database.getOrDefault(userId, new ArrayList<String>());
        user.remove(url);
        database.put(userId, user);
    }
}
