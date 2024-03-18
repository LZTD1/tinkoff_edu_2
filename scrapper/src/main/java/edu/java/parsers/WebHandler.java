package edu.java.parsers;

import edu.java.bot.dto.LinkUpdate;
import edu.java.database.dto.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface WebHandler {
    String getHost();

    List<LinkUpdate> getUpdate(Link link);
}
