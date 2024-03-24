package converters;

import java.net.URI;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;

public class UriConverter implements Converter<String, URI> {
    @Override
    public URI from(String s) {
        return URI.create(s);
    }

    @Override
    public String to(URI uri) {
        return uri.toString();
    }

    @Override
    public @NotNull Class<String> fromType() {
        return String.class;
    }

    @Override
    public @NotNull Class<URI> toType() {
        return URI.class;
    }
}
