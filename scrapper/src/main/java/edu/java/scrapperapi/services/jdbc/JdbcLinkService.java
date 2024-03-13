package edu.java.scrapperapi.services.jdbc;

import edu.java.database.dto.Link;
import edu.java.domain.LinksDao;
import edu.java.domain.UserLinkRelationDao;
import edu.java.domain.UsersDao;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapperapi.exceptions.LinkAlreadyExistsException;
import edu.java.scrapperapi.exceptions.UserIsNotDefindedException;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
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
            setLink(url);
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
    public LinkResponse remove(long tgChatId, URI url) {

        LinkResponse link = linksDao.getLinkByLink(url.toString());
        userLinkRelationDao.deleteRelational(
            usersDao.getUserByTgId(tgChatId),
            link
        );

        return link;
    }

    @Override
    @Transactional
    public List<LinkResponse> listAll(long tgChatId, int limit, int offset) {
        try {
            return userLinkRelationDao.getAllRelational(limit, offset)
                .stream()
                .map(entry -> linksDao.getLinkById(entry.getLink().getId()))
                .toList();
        }catch (NoSuchElementException e){
            throw new UserIsNotDefindedException();
        }
    }

    @Override
    public List<Link> listScheduler(int minutes) {
        List<Link> result = linksDao.getAllLinksNotUpdates(minutes);
        return result;
    }

}