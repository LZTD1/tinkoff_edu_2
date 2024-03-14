package edu.java.domain;

import edu.java.database.dto.Link;
import edu.java.scrapper.dto.LinkResponse;
import java.net.URI;
import java.sql.Types;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
public class LinksDao {

    public static final String LINK = "link";
    public static final String LASTHASH = "lasthash";
    private final JdbcTemplate template;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public LinksDao(JdbcTemplate template, TransactionTemplate transactionTemplate) {
        this.template = template;
        this.transactionTemplate = transactionTemplate;
    }

    @Transactional
    public Long createLink(Link link) {
        String sql =
            "INSERT INTO links (link) VALUES ( ? ) ON CONFLICT (link) DO UPDATE SET link = EXCLUDED.link RETURNING id;";
        return template.queryForObject(sql, Long.class, link.getLink().toString());
    }

    @Transactional
    public void deleteLink(Link link) {
        String sql = "DELETE FROM links WHERE link = ?";
        template.update(sql, link.getLink().toString());
    }

    @Transactional
    public List<Link> getAllLinks(int limit, int offset) {
        String sql = "SELECT * FROM links LIMIT ? OFFSET ?;";
        return template.query(sql, (rs, rowNum) -> {
            Link link = new Link();
            link.setId(rs.getLong("id"));
            link.setLink(URI.create(rs.getObject(LINK, String.class)));
            link.setLasthash(rs.getObject(LASTHASH, String.class));
            return link;
        }, limit, offset);
    }

    @Transactional
    public LinkResponse getLinkById(Long id) {
        String sql = "SELECT * FROM links WHERE id = ?;";
        return template.query(sql, (rs, rowNum) -> {
            LinkResponse link = new LinkResponse();
            link.setId(rs.getLong("id"));
            link.setUrl(URI.create(rs.getObject(LINK, String.class)));
            return link;
        }, id).getFirst();
    }

    @Transactional
    public LinkResponse getLinkByLink(String url) {
        String sql = "SELECT * FROM links WHERE link = ?;";
        return template.query(sql, (rs, rowNum) -> {
            LinkResponse link = new LinkResponse();
            link.setId(rs.getLong("id"));
            link.setUrl(URI.create(rs.getObject(LINK, String.class)));
            return link;
        }, url).getFirst();
    }

    @Transactional
    public List<Link> getAllLinksNotUpdates(int minutes) {
        String sql = "SELECT * FROM links WHERE current_timestamp - updatetime > INTERVAL '" + minutes + " minutes';";
        return template.query(sql, (rs, rowNum) -> {
            Link link = new Link();
            link.setId(rs.getLong("id"));
            link.setLink(URI.create(rs.getObject(LINK, String.class)));
            link.setLasthash(rs.getObject(LASTHASH, String.class));
            return link;
        });
    }

    @Transactional
    public void updateTimeAndLastHash(Long idLink, String hash) {
        String sql = "UPDATE links SET lasthash = ?, updatetime = current_timestamp WHERE id = ?;";
        template.update(sql, new Object[] {hash, idLink}, new int[] {
            Types.VARCHAR,
            Types.BIGINT
        });
    }
}
