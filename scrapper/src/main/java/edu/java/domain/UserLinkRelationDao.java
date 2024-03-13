package edu.java.domain;

import edu.java.database.dto.User;
import edu.java.database.dto.UserLinkRel;
import edu.java.scrapper.dto.LinkResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
public class UserLinkRelationDao {

    public static final String USERID = "userid";
    public static final String LINKID = "linkid";
    private final JdbcTemplate template;
    private final UsersDao usersDao;
    private final LinksDao linksDao;

    @Autowired
    public UserLinkRelationDao(
        JdbcTemplate template,
        UsersDao usersDao,
        LinksDao linksDao
    ) {
        this.template = template;
        this.usersDao = usersDao;
        this.linksDao = linksDao;
    }

    @Transactional
    public void createRelational(User user, LinkResponse link) {
        String sql = "INSERT INTO users_links (userid, linkid) VALUES (?, ?)";
        template.update(sql, user.getId(), link.getId());
    }

    @Transactional
    public void deleteRelational(User user, LinkResponse link) {
        String sql = "DELETE FROM users_links WHERE userid = ? AND linkid = ?";
        template.update(sql, user.getId(), link.getId());
    }

    @Transactional
    public List<UserLinkRel> getAllRelational(int limit, int offset) {
        String sql = "SELECT * FROM users_links LIMIT ? OFFSET ?;";
        return template.query(sql, (rs, rowNum) -> {
            User user = usersDao.getUserById(rs.getLong(USERID));
            LinkResponse link = linksDao.getLinkById(rs.getLong(LINKID));

            return new UserLinkRel() {{
                setLink(link);
                setUser(user);
            }};
        }, limit, offset);
    }

    @Transactional
    public List<UserLinkRel> getAllLinksByTgId(Long tgId, int limit, int offset) {
        Long userID = usersDao.getUserByTgId(tgId).getId();

        String sql = "SELECT * FROM users_links WHERE userid = ? LIMIT ? OFFSET ?;";
        return template.query(sql, (rs, rowNum) -> {

            User user = usersDao.getUserById(rs.getLong(USERID));
            LinkResponse link = linksDao.getLinkById(rs.getLong(LINKID));

            return new UserLinkRel() {{
                setLink(link);
                setUser(user);
            }};
        }, userID, limit, offset);
    }
}
