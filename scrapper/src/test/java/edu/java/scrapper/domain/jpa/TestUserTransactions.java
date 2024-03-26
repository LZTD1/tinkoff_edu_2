package edu.java.scrapper.domain.jpa;

import edu.java.dto.User;
import edu.java.domain.jpa.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testDeleteByTelegramId() {
        jdbcTemplate.update("INSERT INTO users(telegramid) values (123)");
        User user = userRepository.getUserByTelegramId(123L);

        assertThat(user.getTelegramId()).isEqualTo(123L);
    }

    @Test
    @Transactional
    @Rollback
    void testGetUserByTelegramId() {
        jdbcTemplate.update("INSERT INTO users(telegramid) values (123)");

        var rows = userRepository.deleteByTelegramId(123L);

        assertThat(rows).isOne();
    }
}
