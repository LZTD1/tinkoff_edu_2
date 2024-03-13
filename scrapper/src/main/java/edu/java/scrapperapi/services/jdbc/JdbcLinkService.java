package edu.java.scrapperapi.services.jdbc;

import edu.java.database.dto.Link;
import edu.java.domain.LinksDao;
import edu.java.domain.UserLinkRelationDao;
import edu.java.domain.UsersDao;
import edu.java.scrapperapi.exceptions.LinkAlreadyExistsException;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcLinkService implements LinkService {

    private LinksDao linksDao;
    private UsersDao usersDao;
    private UserLinkRelationDao userLinkRelationDao;

    @Autowired
    public JdbcLinkService(LinksDao linksDao, UsersDao usersDao, UserLinkRelationDao userLinkRelationDao) {
        this.linksDao = linksDao;
        this.usersDao = usersDao;
        this.userLinkRelationDao = userLinkRelationDao;
    }

    @Override
    @Transactional
    public Long createLink(long tgChatId, URI url) {
        Long idLink = linksDao.createLink(new Link() {{
            setLink(url.toString());
        }});

        try {
            userLinkRelationDao.createRelational(
                usersDao.getUserByTgId(tgChatId),
                linksDao.getLinkById(idLink)
            );
        } catch (DuplicateKeyException e) {
            throw new LinkAlreadyExistsException("Current link already tracked!");
        }

        return idLink;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI url) {
        var link = linksDao.getLinkByLink(url.toString());
        userLinkRelationDao.deleteRelational(
            usersDao.getUserByTgId(tgChatId),
            link
        );

        return link;
    }

    @Override
    @Transactional
    public List<Link> listAll(long tgChatId, int limit, int offset) {
        return userLinkRelationDao.getAllLinksByTgId(tgChatId, limit, offset)
            .stream()
            .map(entry -> linksDao.getLinkById(entry.getLink().getId()))
            .toList();
    }
}
