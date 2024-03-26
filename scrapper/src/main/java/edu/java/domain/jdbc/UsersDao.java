package edu.java.domain.jdbc;

import edu.java.dto.User;
import edu.java.domain.jdbc.mappers.UserMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UsersDao {

    private final JdbcTemplate template;

    @Autowired
    public UsersDao(JdbcTemplate template) {
        this.template = template;
    }

    @Transactional
    public Long createUser(User user) {
        String sql = "INSERT INTO users (telegramid) VALUES (?) RETURNING id";
        return template.queryForObject(sql, Long.class, user.getTelegramId());
    }

    @Transactional
    public int deleteUser(User user) {
        String sql = "DELETE FROM users WHERE telegramid = ?";
        return template.update(sql, user.getTelegramId());
    }

    @Transactional
    public List<User> getAllUsers(int limit, int offset) {
        String sql = "SELECT * FROM users LIMIT ? OFFSET ?;";
        return template.query(sql, UserMapper::map, limit, offset);
    }

    @Transactional
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?;";
        return template.query(sql, UserMapper::map, id).getFirst();
    }

    @Transactional
    public User getUserByTgId(Long tgId) {
        String sql = "SELECT * FROM users WHERE telegramid = ?;";
        return template.query(sql, UserMapper::map, tgId).getFirst();
    }
}
