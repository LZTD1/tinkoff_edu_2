package edu.java.scrapperapi.services.jpa;

import edu.java.database.dto.Link;
import edu.java.database.dto.UserLinkRel;
import edu.java.domain.jpa.LinkRepository;
import edu.java.domain.jpa.UserLinkRelRepository;
import edu.java.domain.jpa.UserRepository;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public class JpaLinkService implements LinkService {

    private LinkRepository linkRepository;
    private UserRepository userRepository;
    private UserLinkRelRepository userLinkRelRepository;


    public JpaLinkService(LinkRepository linkRepository, UserRepository userRepository, UserLinkRelRepository userLinkRelRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.userLinkRelRepository = userLinkRelRepository;
    }

    @Override
    public Long createLink(long tgChatId, URI url) {
        Long idLink = linkRepository.saveAndFlush(new Link() {{
            setLink(url);
        }}).getId();

        userLinkRelRepository.save(new UserLinkRel(){{
            setUser(userRepository.getUserByTelegramId(tgChatId));
            setLink(linkRepository.getReferenceById(idLink));
        }});

        return idLink;
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
