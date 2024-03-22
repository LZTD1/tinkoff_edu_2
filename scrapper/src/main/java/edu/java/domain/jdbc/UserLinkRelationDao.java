package edu.java.domain.jdbc;

import edu.java.database.dto.Link;
import edu.java.database.dto.User;
import edu.java.database.dto.UserLinkRel;
import edu.java.domain.jdbc.mappers.UserLinkRelMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserLinkRelationDao {

    private final JdbcTemplate template;

    @Autowired
    public UserLinkRelationDao(
        JdbcTemplate template
    ) {
        this.template = template;
    }

    @Transactional
    public void createRelational(User user, Link link) {
        String sql = "INSERT INTO users_links (userid, linkid) VALUES (?, ?)";
        template.update(sql, user.getId(), link.getId());
    }

    @Transactional
    public void deleteRelational(User user, Link link) {
        String sql = "DELETE FROM users_links WHERE userid = ? AND linkid = ?";
        template.update(sql, user.getId(), link.getId());
    }

    @Transactional
    public List<UserLinkRel> getAllRelational(int limit, int offset) {
        String sql =
            """
                    SELECT ul.*, u.*, l.*
                    FROM users_links ul
                    JOIN users u ON ul.userid = u.id
                    JOIN links l ON ul.linkid = l.id
                    LIMIT ? OFFSET ?;
                """;
        return template.query(sql, UserLinkRelMapper::map, limit, offset);
    }

    @Transactional
    public List<UserLinkRel> getAllLinksByTgId(Long tgId, int limit, int offset) {
        String sql =
            """
                    SELECT ul.*, u.*, l.*
                    FROM users_links ul
                    JOIN users u ON ul.userid = u.id
                    JOIN links l ON ul.linkid = l.id
                    WHERE u.telegramid = ?
                    LIMIT ? OFFSET ?;
                     """;
        return template.query(sql, UserLinkRelMapper::map, tgId, limit, offset);
    }

    @Transactional
    public List<Long> getAllUsersIdWithLink(Long id) {
        String sql = "SELECT userid FROM users_links WHERE linkid = ?";
        return template.query(sql, (rs, rowNum) -> rs.getLong("userid"), id);
    }
}
