package edu.java.scrapper.domain;

import edu.java.database.dto.User;
import edu.java.domain.UsersDao;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import edu.java.scrapperapi.exceptions.EntityAlreadyExistsError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TestUserTransactions extends IntegrationTest {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testAdd() {
        usersDao.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );

        int result = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE telegramid = ?",
            Long.class,
            505
        ).intValue();

        assertThat(result).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void testAddException() {
        usersDao.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        assertThrows(EntityAlreadyExistsError.class, () -> {
            usersDao.createUser(
                new User() {{
                    setTelegramId(505L);
                }}
            );
        });
    }

    @Test
    @Transactional
    @Rollback
    void testDelete() {
        usersDao.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        usersDao.deleteUser(
            new User() {{
                setTelegramId(505L);
            }}
        );

        int result = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE telegramid = ?",
            Long.class,
            505
        ).intValue();

        assertThat(result).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void testFindAll() {
        for (int i = 0; i < 5; i++) {
            usersDao.createUser(
                new User() {{
                    setTelegramId(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE));
                }}
            );
        }

        List<User> result = usersDao.getAllUsers(3, 0);
        assertThat(result.size()).isEqualTo(3);

        List<User> result2 = usersDao.getAllUsers(3, 3);
        assertThat(result2.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @Rollback
    void testGetById() {
        Long id = usersDao.createUser(
            new User() {{
                setTelegramId(1L);
            }}
        );

        User dbObject = usersDao.getUserById(id);

        assertThat(dbObject.getTelegramId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    void testGetByTgId() {
        Long id = usersDao.createUser(
            new User() {{
                setTelegramId(5L);
            }}
        );

        User dbObject = usersDao.getUserByTgId(5L);

        assertThat(dbObject.getId()).isEqualTo(id);
    }
}
