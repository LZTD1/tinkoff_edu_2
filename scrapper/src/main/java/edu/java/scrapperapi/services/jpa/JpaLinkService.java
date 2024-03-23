package edu.java.scrapperapi.services.jpa;

import edu.java.database.dto.Link;
import edu.java.database.dto.User;
import edu.java.database.dto.UserLinkRel;
import edu.java.database.dto.UsersLinkId;
import edu.java.domain.jdbc.mappers.LinkResponseMapper;
import edu.java.domain.jpa.LinkRepository;
import edu.java.domain.jpa.UserLinkRelRepository;
import edu.java.domain.jpa.UserRepository;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapperapi.exceptions.LinkAlreadyExistsException;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

public class JpaLinkService implements LinkService {

    private LinkRepository linkRepository;
    private UserRepository userRepository;
    private UserLinkRelRepository userLinkRelRepository;

    public JpaLinkService(
        LinkRepository linkRepository,
        UserRepository userRepository,
        UserLinkRelRepository userLinkRelRepository
    ) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.userLinkRelRepository = userLinkRelRepository;
    }

    @Override
    @Transactional
    public Long createLink(long tgChatId, URI url) {
        Link existingLink = linkRepository.findLinkByLink(url);

        if (existingLink == null) {
            var newLink = new Link();
            newLink.setLink(url);

            existingLink = linkRepository.saveAndFlush(newLink);
        }

        UserLinkRel userLinkRel = new UserLinkRel();
        userLinkRel.setLinkid(existingLink);
        userLinkRel.setUserid(userRepository.getUserByTelegramId(tgChatId));

        try {
            userLinkRelRepository.saveAndFlush(userLinkRel);
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

        List<UserLinkRel> response = userLinkRelRepository.findAllByUserid_TelegramId(tgChatId);

        return response.stream()
            .map(e -> LinkResponseMapper.map(e.getLinkid()))
            .toList();
    }

    @Override
    public List<Link> listScheduler(int minutes, int limit) {
        return linkRepository.getLinksNotUpdates(Duration.ofMinutes(minutes));
    }

    @Override
    public void updateLastSendTime(Long idLink, OffsetDateTime newSendTime) {
        linkRepository.updateLastsendtimeById(newSendTime, idLink);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getAllUsersWithLink(Link link) {

        return userLinkRelRepository
            .findByLinkid_Id(link.getId())
            .stream().map(e -> e.getUserid().getTelegramId())
            .toList();
    }
}
