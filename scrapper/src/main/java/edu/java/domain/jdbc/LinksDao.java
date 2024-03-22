package edu.java.domain.jdbc;

import edu.java.database.dto.Link;
import edu.java.domain.jdbc.mappers.LinkMapper;
import edu.java.domain.jdbc.mappers.LinkResponseMapper;
import edu.java.scrapper.dto.LinkResponse;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LinksDao {

    private final JdbcTemplate template;

    @Autowired
    public LinksDao(JdbcTemplate template) {
        this.template = template;
    }

    @Transactional
    public Long createLink(Link link) {
        String sql =
            "INSERT INTO links (link) VALUES ( ? ) ON CONFLICT (link) DO UPDATE SET link = EXCLUDED.link RETURNING id;";
        return template.queryForObject(sql, Long.class, link.getLink().toString());
    }

    @Transactional
    public List<Link> getAllLinks(int limit, int offset) {
        String sql = "SELECT * FROM links LIMIT ? OFFSET ?;";
        return template.query(sql, LinkMapper::map, limit, offset);
    }

    @Transactional
    public LinkResponse getLinkById(Long id) {
        String sql = "SELECT * FROM links WHERE id = ?;";
        return template.query(sql, LinkResponseMapper::map, id).getFirst();
    }

    @Transactional
    public LinkResponse getLinkByLink(String url) {
        String sql = "SELECT * FROM links WHERE link = ?;";
        return template.query(sql, LinkResponseMapper::map, url).getFirst();
    }

    @Transactional
    public List<Link> getLinksNotUpdates(int minutes, int limit) {
        String sql = "SELECT * FROM links WHERE current_timestamp - updatetime > INTERVAL '" + minutes + " minutes';";
        return template.query(sql, LinkMapper::map);
    }

    @Transactional
    public void updateLastSendTime(Long idLink, OffsetDateTime dateTime) {
        String sql = "UPDATE links SET lastsendtime = ?, updatetime = current_timestamp WHERE id = ?;";
        template.update(sql, new Object[] {Timestamp.from(dateTime.toInstant()), idLink}, new int[] {
            Types.TIMESTAMP,
            Types.BIGINT
        });
    }
}
