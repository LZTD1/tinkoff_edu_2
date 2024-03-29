package edu.java.dto.converters;

import edu.java.scrapperapi.exceptions.LinkIsNotValidException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.net.MalformedURLException;
import java.net.URI;

@Converter
public class UriConverter implements AttributeConverter<URI, String> {
    @Override
    public String convertToDatabaseColumn(URI uri) {
        return uri.toString();
    }

    @Override
    public URI convertToEntityAttribute(String s) {
        var uri = URI.create(s);
        try {
            uri.toURL();
            return uri;
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new LinkIsNotValidException(e);
        }
    }
}
