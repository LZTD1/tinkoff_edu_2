package edu.java.scrapperapi.services.jpa;

import edu.java.domain.jdbc.mappers.LinkResponseMapper;
import edu.java.domain.jpa.LinkRepository;
import edu.java.domain.jpa.UserLinkRelRepository;
import edu.java.domain.jpa.UserRepository;
import edu.java.dto.Link;
import edu.java.dto.User;
import edu.java.dto.UserLinkRel;
import edu.java.dto.UsersLinkId;
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

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final UserLinkRelRepository userLinkRelRepository;

    @Override
    @Transactional
    public Long createLink(long tgChatId, URI url) {
        Link existingLink = linkRepository.findLinkByLink(url);

        if (existingLink == null) {
            var newLink = new Link();
            newLink.setLink(url);

            existingLink = linkRepository.save(newLink);
        }

        UserLinkRel userLinkRel = new UserLinkRel();
        userLinkRel.setLink(existingLink);
        userLinkRel.setUser(userRepository.getUserByTelegramId(tgChatId));

        try {
            userLinkRelRepository.save(userLinkRel);
        } catch (DataIntegrityViolationException e) {
            throw new LinkAlreadyExistsException("Current link already tracked!");
        }

        return existingLink.getId();
    }

    @Override
    public LinkResponse remove(long tgChatId, URI url) {
        Link existingLink = linkRepository.findLinkByLink(url);
        User user = userRepository.getUserByTelegramId(tgChatId);

        UserLinkRel userLinkRel = new UserLinkRel();
        userLinkRel.setId(new UsersLinkId() {{
            setLinkid(existingLink.getId());
            setUserid(user.getId());
        }});

        userLinkRelRepository.delete(userLinkRel);

        return LinkResponseMapper.map(existingLink);
    }

    @Override
    public List<LinkResponse> listAll(long tgChatId, int limit, int offset) {

        List<UserLinkRel> response = userLinkRelRepository.findByUserTelegramId(tgChatId);

        return response.stream()
            .map(e -> LinkResponseMapper.map(e.getLink()))
            .skip(offset)
            .limit(limit)
            .toList();
    }

    @Override
    public List<Link> listScheduler(int minutes, int limit) {
        return linkRepository.getLinksNotUpdates(Duration.ofMinutes(minutes))
            .stream()
            .limit(limit)
            .toList();
    }

    @Override
    public void updateLastSendTime(Long idLink, OffsetDateTime newSendTime) {
        linkRepository.updateLastsendtimeById(newSendTime, idLink);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getAllUsersWithLink(Link link) {

        return userLinkRelRepository
            .findByLinkId(link.getId())
            .stream().map(e -> e.getUser().getTelegramId())
            .toList();
    }
}
