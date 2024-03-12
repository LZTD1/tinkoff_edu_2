package edu.java.scrapper.domain;

import edu.java.database.dto.Link;
import edu.java.domain.LinksDao;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestLinkTransactions extends IntegrationTest {

    @Autowired
    private LinksDao linksDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testAdd() {
        linksDao.createLink(
            new Link() {{
                setLink("vk.com");
            }}
        );

        int result = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM links WHERE link = ?",
            Long.class,
            "vk.com"
        ).intValue();

        assertThat(result).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void testDelete() {
        linksDao.createLink(
            new Link() {{
                setLink("vk.com");
            }}
        );
        linksDao.deleteLink(
            new Link() {{
                setLink("vk.com");
            }}
        );

        int result = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM links WHERE link = ?",
            Long.class,
            "vk.com"
        ).intValue();

        assertThat(result).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void testFindAll() {
        for (int i = 0; i < 5; i++) {
            linksDao.createLink(
                new Link() {{
                    setLink(UUID.randomUUID().toString());
                }}
            );
        }

        List<Link> result = linksDao.getAllLinks(3, 0);
        assertThat(result.size()).isEqualTo(3);

        List<Link> result2 = linksDao.getAllLinks(3, 3);
        assertThat(result2.size()).isEqualTo(2);
    }
}
