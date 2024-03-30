package edu.java.scrapper.domain.jpa;

import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.dto.Link;
import edu.java.scrapper.domain.IntegrationTest;
import java.net.URI;
import java.time.Duration;
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
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testFindLinkByLink() {
        String url = "https://vk.com";

        jdbcTemplate.update("INSERT INTO links (link) VALUES ('" + url + "') ");

        Link link = jpaLinkRepository.findLinkByLink(URI.create(url));

        assertThat(link.getLink()).isEqualTo(URI.create(url));
    }

    @Test
    @Transactional
    @Rollback
    void testGetLinksNotUpdates() {
        String url = "https://vk.com";

        jdbcTemplate.update(
            "INSERT INTO links (link, updatetime) VALUES ('" + url + "', '2023-03-13 21:39:44.907092 +03:00') ");

        List<Link> links = jpaLinkRepository.getLinksNotUpdates(Duration.ofMinutes(5));

        assertThat(links.size()).isEqualTo(1);
        assertThat(links.getFirst().getLink()).isEqualTo(URI.create(url));
    }

    @Test
    @Transactional
    @Rollback
    void testUpdateLastsendtimeById() {
        String url = "https://vk.com";
        String offsetDateTime = "2023-03-13 21:39:44.907092 +03:00";

        Long id = jdbcTemplate.queryForObject(
            "INSERT INTO links (link, updatetime, lastsendtime) VALUES ('" + url + "', '" + offsetDateTime + "', '" +
                offsetDateTime + "') RETURNING id;",
            (rs, rowNum) -> rs.getLong("id")
        );
        jpaLinkRepository.updateLastsendtimeById(OffsetDateTime.now(), id);

        Link link = jpaLinkRepository.getReferenceById(id);

        assertThat(link.getLastsendtime().toString()).isNotEqualTo(offsetDateTime);
        assertThat(link.getUpdatetime().toString()).isNotEqualTo(offsetDateTime);
    }

}
