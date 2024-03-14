package edu.java.parsers;

import java.net.URI;
import java.util.Optional;

public interface WebHandler {
    String getHost();

    Optional<String> getUpdateReasonIfHas(URI uri);
}
