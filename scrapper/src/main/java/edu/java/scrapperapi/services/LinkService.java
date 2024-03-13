package edu.java.scrapperapi.services;

import edu.java.database.dto.Link;
import java.net.URI;
import java.util.List;

public interface LinkService {
    Long createLink(long tgChatId, URI url);

    Link remove(long tgChatId, URI url);

    List<Link> listAll(long tgChatId, int limit, int offset);
}
