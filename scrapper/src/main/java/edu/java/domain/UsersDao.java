package edu.java.domain;

import edu.java.database.dto.User;
import edu.java.domain.mappers.UserMapper;
import edu.java.scrapperapi.exceptions.EntityAlreadyExistsError;
import edu.java.scrapperapi.exceptions.EntityDeleteException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
        try {
            return template.queryForObject(sql, Long.class, user.getTelegramId());
        } catch (DuplicateKeyException e) {
            throw new EntityAlreadyExistsError("Пользователь с таким telegramId уже существует!");
        }
    }

    @Transactional
    public void deleteUser(User user) {
        String sql = "DELETE FROM users WHERE telegramid = ?";
        int rowAffected = template.update(sql, user.getTelegramId());
        if (rowAffected == 0) {
            throw new EntityDeleteException("Не возможно удалить юзера, возможн он уже был удален!");
        }
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
