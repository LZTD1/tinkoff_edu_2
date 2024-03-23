package edu.java.parsers;

import edu.java.bot.dto.LinkUpdate;
import edu.java.database.dto.Link;
import java.util.List;

public interface WebHandler {
    String getHost();

    List<LinkUpdate> getUpdate(Link link);
}
