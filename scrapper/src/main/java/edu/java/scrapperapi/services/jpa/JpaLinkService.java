package edu.java.scrapperapi.services.jpa;

import edu.java.database.dto.Link;
import edu.java.database.dto.UserLinkRel;
import edu.java.domain.jpa.LinkRepository;
import edu.java.domain.jpa.UserLinkRelRepository;
import edu.java.domain.jpa.UserRepository;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapperapi.exceptions.LinkAlreadyExistsException;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
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
        }catch (DataIntegrityViolationException e){
            throw new LinkAlreadyExistsException("Current link already tracked!");
        }

        return existingLink.getId();
    }

    @Override
    public LinkResponse remove(long tgChatId, URI url) {
        return null;
    }

    @Override
    public List<LinkResponse> listAll(long tgChatId, int limit, int offset) {
        return null;
    }

    @Override
    public List<Link> listScheduler(int minutes, int limit) {
        return null;
    }

    @Override
    public void updateLastSendTime(Long idLink, OffsetDateTime newSendTime) {

    }

    @Override
    public List<Long> getAllUsersWithLink(Link link) {
        return null;
    }
}
