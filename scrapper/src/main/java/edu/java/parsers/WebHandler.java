package edu.java.parsers;

import edu.java.database.dto.Link;
import java.net.URI;
import java.util.Optional;

public interface WebHandler {
    String getHost();

    void getUpdate(Link link);
    Optional<String> getUpdateReasonIfHas(URI uri);
}
