package database;

public interface Database {

    void registerUser(Long userId);

    Object getUserLinksById(Long userId);

    void addLink(Long userId, String url);

    void removeLink(Long userId, String url);
}
