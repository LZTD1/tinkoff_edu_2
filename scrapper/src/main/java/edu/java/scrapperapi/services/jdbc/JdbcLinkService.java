package edu.java.scrapperapi.services.jdbc;

import edu.java.database.dto.Link;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcUserLinkRelRepository;
import edu.java.domain.jdbc.JdbcUserRepository;
import edu.java.domain.jdbc.mappers.LinkResponseMapper;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapperapi.exceptions.LinkAlreadyExistsException;
import edu.java.scrapperapi.exceptions.UserIsNotDefindedException;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcUserRepository jdbcUserRepository;
    private final JdbcUserLinkRelRepository jdbcUserLinkRelRepository;

    @Override
    @Transactional
    public Long createLink(long tgChatId, URI url) {
        Long idLink = jdbcLinkRepository.createLink(new Link() {{
            setLink(url);
        }});

        try {
            jdbcUserLinkRelRepository.createRelational(
                jdbcUserRepository.getUserByTgId(tgChatId),
                jdbcLinkRepository.getLinkById(idLink)
            );
        } catch (DuplicateKeyException e) {
            throw new LinkAlreadyExistsException("Current link already tracked!");
        }

        return idLink;
    }

    @Override
    @Transactional
    public LinkResponse remove(long tgChatId, URI url) {

        Link link = jdbcLinkRepository.getLinkByLink(url.toString());
        jdbcUserLinkRelRepository.deleteRelational(
            jdbcUserRepository.getUserByTgId(tgChatId),
            link
        );

        return LinkResponseMapper.map(link);
    }

    @Override
    @Transactional
    public List<LinkResponse> listAll(long tgChatId, int limit, int offset) {
        try {

            return jdbcUserLinkRelRepository.getAllLinksByTgId(tgChatId, limit, offset)
                .stream()
                .map(entry -> LinkResponseMapper.map(
                    entry.getLink()
                ))
                .toList();

        } catch (NoSuchElementException e) {
            throw new UserIsNotDefindedException();
        }
    }

    @Override
    public List<Link> listScheduler(int minutes, int limit) {
        return jdbcLinkRepository.getLinksNotUpdates(minutes, limit);
    }

    @Override
    public void updateLastSendTime(Long idLink, OffsetDateTime newSendTime) {
        jdbcLinkRepository.updateLastSendTime(idLink, newSendTime);
    }

    @Override
    public List<Long> getAllUsersWithLink(Link link) {
        return jdbcUserLinkRelRepository.getAllUsersIdWithLink(link.getId())
            .stream()
            .map(entry -> jdbcUserRepository.getUserById(entry).getTelegramId())
            .toList();
    }

}
