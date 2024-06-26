package edu.java.scrapperapi.services;

import edu.java.dto.Link;
import edu.java.scrapper.dto.LinkResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    Long createLink(long tgChatId, URI url);

    LinkResponse remove(long tgChatId, URI url);

    List<LinkResponse> listAll(long tgChatId, int limit, int offset);

    List<Link> listScheduler(int minutes, int limit);

    void updateLastSendTime(Long idLink, OffsetDateTime newSendTime);

    List<Long> getAllUsersWithLink(Link link);
}
