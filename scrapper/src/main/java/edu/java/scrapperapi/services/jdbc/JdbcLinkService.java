package edu.java.scrapperapi.services.jdbc;

import edu.java.database.dto.Link;
import edu.java.domain.jdbc.LinksDao;
import edu.java.domain.jdbc.UserLinkRelationDao;
import edu.java.domain.jdbc.UsersDao;
import edu.java.domain.jdbc.mappers.LinkResponseMapper;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapperapi.exceptions.LinkAlreadyExistsException;
import edu.java.scrapperapi.exceptions.UserIsNotDefindedException;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

public class JdbcLinkService implements LinkService {

    private LinksDao linksDao;
    private UsersDao usersDao;
    private UserLinkRelationDao userLinkRelationDao;

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

        Link link = linksDao.getLinkByLink(url.toString());
        userLinkRelationDao.deleteRelational(
            usersDao.getUserByTgId(tgChatId),
            link
        );

        return LinkResponseMapper.map(link);
    }

    @Override
    @Transactional
    public List<LinkResponse> listAll(long tgChatId, int limit, int offset) {
        try {

            return userLinkRelationDao.getAllLinksByTgId(tgChatId, limit, offset)
                .stream()
                .map(entry -> LinkResponseMapper.map(
                    entry.getLinkid()
                ))
                .toList();

        } catch (NoSuchElementException e) {
            throw new UserIsNotDefindedException();
        }
    }

    @Override
    public List<Link> listScheduler(int minutes, int limit) {
        return linksDao.getLinksNotUpdates(minutes, limit);
    }

    @Override
    public void updateLastSendTime(Long idLink, OffsetDateTime newSendTime) {
        linksDao.updateLastSendTime(idLink, newSendTime);
    }

    @Override
    public List<Long> getAllUsersWithLink(Link link) {
        return userLinkRelationDao.getAllUsersIdWithLink(link.getId())
            .stream()
            .map(entry -> usersDao.getUserById(entry).getTelegramId())
            .toList();
    }

}
