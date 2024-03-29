package edu.java.scrapperapi.services.jpa;

import edu.java.database.dto.Link;
import edu.java.database.dto.User;
import edu.java.database.dto.UserLinkRel;
import edu.java.database.dto.UsersLinkId;
import edu.java.domain.jdbc.mappers.LinkResponseMapper;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaUserLinkRelRepository;
import edu.java.domain.jpa.JpaUserRepository;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapperapi.exceptions.LinkAlreadyExistsException;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {

    private final JpaLinkRepository jpaLinkRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaUserLinkRelRepository jpaUserLinkRelRepository;

    @Override
    @Transactional
    public Long createLink(long tgChatId, URI url) {
        Link existingLink = jpaLinkRepository.findLinkByLink(url);

        if (existingLink == null) {
            var newLink = new Link();
            newLink.setLink(url);

            existingLink = jpaLinkRepository.save(newLink);
        }

        UserLinkRel userLinkRel = new UserLinkRel();
        userLinkRel.setLink(existingLink);
        userLinkRel.setUser(jpaUserRepository.getUserByTelegramId(tgChatId));

        try {
            jpaUserLinkRelRepository.save(userLinkRel);
        } catch (DataIntegrityViolationException e) {
            throw new LinkAlreadyExistsException("Current link already tracked!");
        }

        return existingLink.getId();
    }

    @Override
    public LinkResponse remove(long tgChatId, URI url) {
        Link existingLink = jpaLinkRepository.findLinkByLink(url);
        User user = jpaLinkRepository.getUserByTelegramId(tgChatId);

        UserLinkRel userLinkRel = new UserLinkRel();
        userLinkRel.setId(new UsersLinkId() {{
            setLinkid(existingLink.getId());
            setUserid(user.getId());
        }});

        jpaUserLinkRelRepository.delete(userLinkRel);

        return LinkResponseMapper.map(existingLink);
    }

    @Override
    public List<LinkResponse> listAll(long tgChatId, int limit, int offset) {

        List<UserLinkRel> response = jpaUserLinkRelRepository.findByUserTelegramId(tgChatId);

        return response.stream()
            .map(e -> LinkResponseMapper.map(e.getLink()))
            .skip(offset)
            .limit(limit)
            .toList();
    }

    @Override
    public List<Link> listScheduler(int minutes, int limit) {
        return jpaLinkRepository.getLinksNotUpdates(Duration.ofMinutes(minutes))
            .stream()
            .limit(limit)
            .toList();
    }

    @Override
    public void updateLastSendTime(Long idLink, OffsetDateTime newSendTime) {
        jpaLinkRepository.updateLastsendtimeById(newSendTime, idLink);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getAllUsersWithLink(Link link) {

        return jpaUserLinkRelRepository
            .findByLinkId(link.getId())
            .stream().map(e -> e.getUser().getTelegramId())
            .toList();
    }
}
