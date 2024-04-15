package edu.java.scrapper.domain.jooq;

import edu.java.domain.jooq.JooqLinkRepository;
import edu.java.dto.Link;
import edu.java.scrapper.domain.IntegrationTest;
import java.net.URI;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.List;
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
    private JooqLinkRepository jooqLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testAdd() {
        jooqLinkRepository.createLink(
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
    void testGetByLink() {
        Long id = jooqLinkRepository.createLink(
            new Link() {{
                setLink(URI.create("vk.com"));
            }}
        );

        Link dbObject = jooqLinkRepository.getLinkByLink("vk.com");

        assertThat(dbObject.getId()).isEqualTo(id);
    }

    @Test
    @Transactional
    @Rollback
    void testGetLinksByTime() {
        jdbcTemplate.update(
            "INSERT INTO public.links (link, updatetime) VALUES ('test.com', '2000-03-13 21:39:44.907092 +03:00');");

        OffsetDateTime now = OffsetDateTime.now();
        jdbcTemplate.update(
            "INSERT INTO public.links (link, updatetime) VALUES (?, ?)",
            new Object[] {"test2.com", Timestamp.from(now.toInstant())},
            new int[] {Types.VARCHAR, Types.TIMESTAMP}
        );

        List<Link> links = jooqLinkRepository.getLinksNotUpdates(5);

        assertThat(links.size()).isEqualTo(2);
    }

    @Test
    @Rollback
    @Transactional
    void testUpdateTimeAndHash() {
        Long id = jooqLinkRepository.createLink(new Link() {{
            setLink(URI.create("vk.com"));
        }});
        Link before = jooqLinkRepository.getLinkByLink("vk.com");

        jooqLinkRepository.updateLastSendTime(before.getId(), OffsetDateTime.now());

        Link after = jooqLinkRepository.getLinkByLink("vk.com");

        assertThat(before.getLastsendtime().toString()).isNotEqualTo(after.getLastsendtime().toString());
    }
}
