package edu.java.scrapper.domain.jooq;

import edu.java.domain.jooq.JooqLinkRepository;
import edu.java.domain.jooq.JooqUserLinkRelRepository;
import edu.java.domain.jooq.JooqUserRepository;
import edu.java.dto.Link;
import edu.java.dto.User;
import edu.java.scrapper.domain.IntegrationTest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestRelationalTransactions extends IntegrationTest {

    @Autowired
    private JooqUserLinkRelRepository jooqUserLinkRelRepository;

    @Autowired
    private JooqUserRepository jooqUserRepository;

    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testAdd() {
        var userId = jooqUserRepository.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var linkId = jooqLinkRepository.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        jooqUserLinkRelRepository.createRelational(
            jooqUserRepository.getUserById(userId),
            jooqLinkRepository.getLinkById(linkId)
        );

        int count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users_links WHERE userid = ? AND linkid = ?",
            Long.class,
            userId,
            linkId
        ).intValue();

        assertThat(count).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void testDelete() {
        var userId = jooqUserRepository.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var linkId = jooqLinkRepository.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        jooqUserLinkRelRepository.createRelational(
            jooqUserRepository.getUserById(userId),
            jooqLinkRepository.getLinkById(linkId)
        );
        jooqUserLinkRelRepository.deleteRelational(
            jooqUserRepository.getUserById(userId),
            jooqLinkRepository.getLinkById(linkId)
        );

        int count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users_links WHERE userid = ? AND linkid = ?",
            Long.class,
            userId,
            linkId
        ).intValue();

        assertThat(count).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void testGetAllUsersIdWithLink() {
        var user1 = jooqUserRepository.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var user2 = jooqUserRepository.createUser(
            new User() {{
                setTelegramId(404L);
            }}
        );
        var link = jooqLinkRepository.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );
        jooqUserLinkRelRepository.createRelational(
            new User() {{
                setId(user1);
            }},
            new Link() {{
                setId(link);
            }}
        );
        jooqUserLinkRelRepository.createRelational(
            new User() {{
                setId(user2);
            }},
            new Link() {{
                setId(link);
            }}
        );

        List<User> result = jooqUserLinkRelRepository.getAllUsersIdWithLink(link);

        assertThat(result.size()).isEqualTo(2);
    }
}
