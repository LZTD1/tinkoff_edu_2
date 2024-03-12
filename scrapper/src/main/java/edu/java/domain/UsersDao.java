package edu.java.domain;

import edu.java.database.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.List;

@Repository
public class UsersDao {

    private final JdbcTemplate template;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public UsersDao(JdbcTemplate template, TransactionTemplate transactionTemplate) {
        this.template = template;
        this.transactionTemplate = transactionTemplate;
    }

    @Transactional
    public Long createUser(User user) {
        String sql = "INSERT INTO users (telegramid) VALUES (?) RETURNING id";
        return template.queryForObject(sql, Long.class, user.getTelegramId());
    }

    @Transactional
    public void deleteUser(User user) {
        String sql = "DELETE FROM users WHERE telegramid = ?";
        template.update(sql, user.getTelegramId());
    }

    @Transactional
    public List<User> getAllUsers(int limit, int offset) {
        String sql = "SELECT * FROM users LIMIT ? OFFSET ?;";
        return template.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setTelegramId(rs.getLong("telegramId"));
            return user;
        }, limit, offset);
    }
}
