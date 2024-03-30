package edu.java.scrapper.domain.jooq;

import edu.java.domain.jooq.JooqUserRepository;
import edu.java.dto.User;
import edu.java.scrapper.domain.IntegrationTest;
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
    private JooqUserRepository jooqUserRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testAdd() {
        jooqUserRepository.createUser(
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
        jooqUserRepository.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        jooqUserRepository.deleteUser(
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
    void testGetByTgId() {
        Long id = jooqUserRepository.createUser(
            new User() {{
                setTelegramId(5L);
            }}
        );

        User dbObject = jooqUserRepository.getUserByTgId(5L);

        assertThat(dbObject.getId()).isEqualTo(id);
    }
}
