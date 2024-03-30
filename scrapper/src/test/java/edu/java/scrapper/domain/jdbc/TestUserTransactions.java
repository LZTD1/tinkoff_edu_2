package edu.java.scrapper.domain.jdbc;

import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.dto.User;
import edu.java.scrapper.domain.IntegrationTest;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestUserTransactions extends IntegrationTest {

    @Autowired
    private JdbcUserRepository jdbcUserRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testAdd() {
        jdbcUserRepository.createUser(
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
    void testDelete() {
        jdbcUserRepository.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        jdbcUserRepository.deleteUser(
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
            jdbcUserRepository.createUser(
                new User() {{
                    setTelegramId(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE));
                }}
            );
        }

        List<User> result = jdbcUserRepository.getAllUsers(3, 0);
        assertThat(result.size()).isEqualTo(3);

        List<User> result2 = jdbcUserRepository.getAllUsers(3, 3);
        assertThat(result2.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @Rollback
    void testGetById() {
        Long id = jdbcUserRepository.createUser(
            new User() {{
                setTelegramId(1L);
            }}
        );

        User dbObject = jdbcUserRepository.getUserById(id);

        assertThat(dbObject.getTelegramId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    void testGetByTgId() {
        Long id = jdbcUserRepository.createUser(
            new User() {{
                setTelegramId(5L);
            }}
        );

        User dbObject = jdbcUserRepository.getUserByTgId(5L);

        assertThat(dbObject.getId()).isEqualTo(id);
    }
}
