package edu.java.scrapper.domain.jdbc;

import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcUserLinkRelRepository;
import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.dto.Link;
import edu.java.dto.User;
import edu.java.dto.UserLinkRel;
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
    private JdbcUserLinkRelRepository jdbcUserLinkRelRepository;

    @Autowired
    private JdbcUserRepository jdbcUserRepository;

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testAdd() {
        var userId = jdbcUserRepository.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var linkId = jdbcLinkRepository.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        jdbcUserLinkRelRepository.createRelational(
            jdbcUserRepository.getUserById(userId),
            jdbcLinkRepository.getLinkById(linkId)
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
        var userId = jdbcUserRepository.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var linkId = jdbcLinkRepository.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        jdbcUserLinkRelRepository.createRelational(
            jdbcUserRepository.getUserById(userId),
            jdbcLinkRepository.getLinkById(linkId)
        );
        jdbcUserLinkRelRepository.deleteRelational(
            jdbcUserRepository.getUserById(userId),
            jdbcLinkRepository.getLinkById(linkId)
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
    void testFindAll() {
        var userId = jdbcUserRepository.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var linkId = jdbcLinkRepository.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        jdbcUserLinkRelRepository.createRelational(
            jdbcUserRepository.getUserById(userId),
            jdbcLinkRepository.getLinkById(linkId)
        );
        List<UserLinkRel> result = jdbcUserLinkRelRepository.getAllRelational(
            5, 0
        );

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getUser().getTelegramId()).isEqualTo(505L);
        assertThat(result.getFirst().getLink().getLink().toString()).isEqualTo("vk.com");
    }

    @Test
    @Transactional
    @Rollback
    void testFindAllByTgId() {
        var user1 = jdbcUserRepository.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var user2 = jdbcUserRepository.createUser(
            new User() {{
                setTelegramId(404L);
            }}
        );
        var link1 = jdbcLinkRepository.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );
        var link2 = jdbcLinkRepository.createLink(
            new Link() {{
                setLink(URI.create("github.com"));
            }}
        );

        jdbcUserLinkRelRepository.createRelational(
            jdbcUserRepository.getUserById(user1),
            jdbcLinkRepository.getLinkById(link1)
        );
        jdbcUserLinkRelRepository.createRelational(
            jdbcUserRepository.getUserById(user2),
            jdbcLinkRepository.getLinkById(link2)
        );

        List<UserLinkRel> result = jdbcUserLinkRelRepository.getAllLinksByTgId(
            505L, 5, 0
        );

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getUser().getTelegramId()).isEqualTo(505L);
        assertThat(result.getFirst().getLink().getLink().toString()).isEqualTo("vk.com");
    }

    @Test
    @Transactional
    @Rollback
    void testGetAllUsersIdWithLink() {
        var user1 = jdbcUserRepository.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var user2 = jdbcUserRepository.createUser(
            new User() {{
                setTelegramId(404L);
            }}
        );
        var link = jdbcLinkRepository.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );
        jdbcUserLinkRelRepository.createRelational(
            new User() {{
                setId(user1);
            }},
            new Link() {{
                setId(link);
            }}
        );
        jdbcUserLinkRelRepository.createRelational(
            new User() {{
                setId(user2);
            }},
            new Link() {{
                setId(link);
            }}
        );
        List<Long> result = jdbcUserLinkRelRepository.getAllUsersIdWithLink(link);

        assertThat(result.size()).isEqualTo(2);
    }
}
