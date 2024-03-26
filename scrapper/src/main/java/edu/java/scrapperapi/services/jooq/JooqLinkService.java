package edu.java.scrapperapi.services.jooq;

import edu.java.database.dto.Link;
import edu.java.domain.jdbc.mappers.LinkMapper;
import edu.java.domain.jdbc.mappers.LinkResponseMapper;
import edu.java.domain.jooq.JooqLinkRepository;
import edu.java.domain.jooq.JooqUserLinkRelRepository;
import edu.java.domain.jooq.JooqUserRepository;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapperapi.exceptions.LinkAlreadyExistsException;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.Record;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.domain.jooq.tables.Tables.LINKS;

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
        Iterator<Record> iterator = jooqUserLinkRelRepository.getAllLinksByTgId(tgChatId, limit, offset);

        ArrayList<LinkResponse> responses = new ArrayList<>();
        while (iterator.hasNext()) {
            responses.add(
                LinkResponseMapper.map(iterator.next())
            );
        }
        return responses;
    }

    @Override
    public List<Link> listScheduler(int minutes, int limit) {
        Iterator<Record> iterator = jooqLinkRepository.getLinksNotUpdates(limit);

        ArrayList<Link> responses = new ArrayList<>();
        while (iterator.hasNext()) {
            Record currentRecord = iterator.next();
            if (Math.abs(
                Duration
                    .between(
                        currentRecord.get(LINKS.UPDATETIME), OffsetDateTime.now()
                    )
                    .toMinutes()) > minutes) {
                responses.add(
                    LinkMapper.mapFromRecord(currentRecord)
                );
            }
        }

        return responses;
    }

    @Override
    public void updateLastSendTime(Long idLink, OffsetDateTime newSendTime) {
        jooqLinkRepository.updateLastSendTime(idLink, newSendTime);
    }

    @Override
    public List<Long> getAllUsersWithLink(Link link) {
        Iterator<Record> iterator = jooqUserLinkRelRepository.getAllUsersIdWithLink(link.getId());

        ArrayList<Long> responses = new ArrayList<>();
        while (iterator.hasNext()) {
            Record currentRecord = iterator.next();
            responses.add(
                (Long) currentRecord.get("telegramid")
            );
        }

        return responses;
    }
}
