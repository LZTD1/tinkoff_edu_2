package edu.java.scrapper.domain;

import edu.java.database.dto.Link;
import edu.java.domain.LinksDao;
import edu.java.scrapper.dto.LinkResponse;
import java.net.URI;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.OffsetDateTime;
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
                setLink(URI.create("vk.com"));
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
    void testFindAll() {
        for (int i = 0; i < 5; i++) {
            linksDao.createLink(
                new Link() {{
                    setLink(URI.create(UUID.randomUUID().toString()));
                }}
            );
        }

        List<Link> result = linksDao.getAllLinks(3, 0);
        assertThat(result.size()).isEqualTo(3);

        List<Link> result2 = linksDao.getAllLinks(3, 3);
        assertThat(result2.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @Rollback
    void testGetById() {
        Long id = linksDao.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        LinkResponse dbObject = linksDao.getLinkById(id);

        assertThat(dbObject.getUrl().toString()).isEqualTo("vk.com");
    }

    @Test
    @Transactional
    @Rollback
    void testGetByLink() {
        Long id = linksDao.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        LinkResponse dbObject = linksDao.getLinkByLink("vk.com");

        assertThat(dbObject.getId()).isEqualTo(id);
    }

    @Test
    @Transactional
    @Rollback
    void testGetLinksByTime() {
        jdbcTemplate.update(
            "INSERT INTO public.links (link, updatetime) VALUES ('test.com', '2023-03-13 21:39:44.907092 +03:00');");

        OffsetDateTime now = OffsetDateTime.now();
        jdbcTemplate.update(
            "INSERT INTO public.links (link, updatetime) VALUES (?, ?)",
            new Object[] {"test2.com", Timestamp.from(now.toInstant())},
            new int[] {Types.VARCHAR, Types.TIMESTAMP}
        );

        List<Link> links = linksDao.getLinksNotUpdates(5, 2);

        assertThat(links.size()).isEqualTo(1);
    }

    @Test
    @Rollback
    @Transactional
    void testUpdateTimeAndHash() {
        Long id = linksDao.createLink(new Link() {{
            setLink(URI.create("vk.com"));
        }});
        Link before = linksDao.getAllLinks(1, 0).getFirst();

        linksDao.updateLastSendTime(before.getId(), OffsetDateTime.now());

        Link after = linksDao.getAllLinks(1, 0).getFirst();

        assertThat(before.getLastsendtime().toString()).isNotEqualTo(after.getLastsendtime().toString());
    }
}
