package edu.java.scrapperapi.services.jooq;

import edu.java.domain.jdbc.mappers.LinkResponseMapper;
import edu.java.domain.jooq.JooqLinkRepository;
import edu.java.domain.jooq.JooqUserLinkRelRepository;
import edu.java.domain.jooq.JooqUserRepository;
import edu.java.dto.Link;
import edu.java.dto.User;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapperapi.exceptions.LinkAlreadyExistsException;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JooqLinkService implements LinkService {

    private final JooqLinkRepository jooqLinkRepository;
    private final JooqUserLinkRelRepository jooqUserLinkRelRepository;
    private final JooqUserRepository jooqUserRepository;

    @Override
    @Transactional
    public Long createLink(long tgChatId, URI url) {
        Long linkId = jooqLinkRepository.createLink(new Link() {{
            setLink(url);
        }});

        try {
            jooqUserLinkRelRepository.createRelational(

                jooqUserRepository.getUserByTgId(tgChatId),
                new Link() {{
                    setId(linkId);
                }}
            );
        } catch (DuplicateKeyException e) {
            throw new LinkAlreadyExistsException("Current link already tracked!");
        }

        return linkId;
    }

    @Override
    @Transactional
    public LinkResponse remove(long tgChatId, URI url) {
        Link link = jooqLinkRepository.getLinkByLink(url.toString());

        jooqUserLinkRelRepository.deleteRelational(
            jooqUserRepository.getUserByTgId(tgChatId),
            link
        );

        return LinkResponseMapper.map(link);
    }

    @Override
    public List<LinkResponse> listAll(long tgChatId, int limit, int offset) {
        return jooqUserLinkRelRepository
            .getAllLinksByTgId(tgChatId, limit, offset)
            .stream().map(LinkResponseMapper::map
            )
            .toList();
    }

    @Override
    public List<Link> listScheduler(int minutes, int limit) {
        List<Link> links = jooqLinkRepository.getLinksNotUpdates(limit);

        links
            .stream()
            .filter((e) -> Math.abs(
                Duration
                    .between(
                        e.getUpdatetime(), OffsetDateTime.now()
                    )
                    .toMinutes()) > minutes)
            .toList();

        return links;
    }

    @Override
    public void updateLastSendTime(Long idLink, OffsetDateTime newSendTime) {
        jooqLinkRepository.updateLastSendTime(idLink, newSendTime);
    }

    @Override
    public List<Long> getAllUsersWithLink(Link link) {
        return jooqUserLinkRelRepository
            .getAllUsersIdWithLink(link.getId())
            .stream()
            .map(User::getTelegramId)
            .toList();
    }
}
