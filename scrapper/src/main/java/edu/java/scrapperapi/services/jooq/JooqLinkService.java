package edu.java.scrapperapi.services.jooq;

import edu.java.database.dto.Link;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapperapi.services.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public class JooqLinkService implements LinkService {
    @Override
    public Long createLink(long tgChatId, URI url) {
        return null;
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
