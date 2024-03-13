package edu.java.parsers;

import java.net.URI;
import java.util.Optional;

public interface WebHandler {
    public String getHost();

    public Optional<String> getUpdateReasonIfHas(URI uri);
}
