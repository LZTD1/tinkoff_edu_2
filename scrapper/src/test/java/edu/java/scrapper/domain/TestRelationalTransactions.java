package edu.java.scrapper.domain;

import edu.java.database.dto.Link;
import edu.java.database.dto.User;
import edu.java.database.dto.UserLinkRel;
import edu.java.domain.LinksDao;
import edu.java.domain.UserLinkRelationDao;
import edu.java.domain.UsersDao;
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
    private UserLinkRelationDao userLinkRelationDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private LinksDao linksDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testAdd() {
        var userId = usersDao.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var linkId = linksDao.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        userLinkRelationDao.createRelational(
            usersDao.getUserById(userId),
            linksDao.getLinkById(linkId)
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
        var userId = usersDao.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var linkId = linksDao.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        userLinkRelationDao.createRelational(
            usersDao.getUserById(userId),
            linksDao.getLinkById(linkId)
        );
        userLinkRelationDao.deleteRelational(
            usersDao.getUserById(userId),
            linksDao.getLinkById(linkId)
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
        var userId = usersDao.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var linkId = linksDao.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        userLinkRelationDao.createRelational(
            usersDao.getUserById(userId),
            linksDao.getLinkById(linkId)
        );
        List<UserLinkRel> result = userLinkRelationDao.getAllRelational(
            5, 0
        );

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getUser().getTelegramId()).isEqualTo(505L);
        assertThat(result.getFirst().getLink().getUrl().toString()).isEqualTo("vk.com");
    }

    @Test
    @Transactional
    @Rollback
    void testFindAllByTgId() {
        var user1 = usersDao.createUser(
            new User() {{
                setTelegramId(505L);
            }}
        );
        var user2 = usersDao.createUser(
            new User() {{
                setTelegramId(404L);
            }}
        );
        var link1 = linksDao.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );
        var link2 = linksDao.createLink(
            new Link() {{
                setLink(URI.create("github.com"));
            }}
        );

        userLinkRelationDao.createRelational(
            usersDao.getUserById(user1),
            linksDao.getLinkById(link1)
        );
        userLinkRelationDao.createRelational(
            usersDao.getUserById(user2),
            linksDao.getLinkById(link2)
        );

        List<UserLinkRel> result = userLinkRelationDao.getAllLinksByTgId(
            505L, 5, 0
        );

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getUser().getTelegramId()).isEqualTo(505L);
        assertThat(result.getFirst().getLink().getUrl().toString()).isEqualTo("vk.com");
    }
}
