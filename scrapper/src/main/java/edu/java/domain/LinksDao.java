package edu.java.domain;

import edu.java.database.dto.Link;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
public class LinksDao {

    public static final String LINK = "link";
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
        return template.queryForObject(sql, Long.class, link.getLink());
    }

    @Transactional
    public void deleteLink(Link link) {
        String sql = "DELETE FROM links WHERE link = ?";
        template.update(sql, link.getLink());
    }

    @Transactional
    public List<Link> getAllLinks(int limit, int offset) {
        String sql = "SELECT * FROM links LIMIT ? OFFSET ?;";
        return template.query(sql, (rs, rowNum) -> {
            Link link = new Link();
            link.setId(rs.getLong("id"));
            link.setLink(rs.getObject(LINK, String.class));
            return link;
        }, limit, offset);
    }

    @Transactional
    public Link getLinkById(Long id) {
        String sql = "SELECT * FROM links WHERE id = ?;";
        return template.query(sql, (rs, rowNum) -> {
            Link link = new Link();
            link.setId(rs.getLong("id"));
            link.setLink(rs.getObject(LINK, String.class));
            return link;
        }, id).getFirst();
    }

    @Transactional
    public Link getLinkByLink(String url) {
        String sql = "SELECT * FROM links WHERE link = ?;";
        return template.query(sql, (rs, rowNum) -> {
            Link link = new Link();
            link.setId(rs.getLong("id"));
            link.setLink(rs.getObject(LINK, String.class));
            return link;
        }, url).getFirst();
    }
}
