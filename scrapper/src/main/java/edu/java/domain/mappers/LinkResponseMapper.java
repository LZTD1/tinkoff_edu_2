package edu.java.domain.mappers;

import edu.java.database.dto.Link;
import edu.java.scrapper.dto.LinkResponse;
import java.net.URI;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkResponseMapper {
    @SneakyThrows
    public static LinkResponse map(ResultSet rs, int rowNum) {
        LinkResponse link = new LinkResponse();

        link.setId(rs.getLong("id"));
        link.setUrl(URI.create(rs.getObject("link", String.class)));

        return link;
    }

    @SneakyThrows
    public static LinkResponse map(Link link) {
        LinkResponse newLink = new LinkResponse();

        newLink.setId(link.getId());
        newLink.setUrl(link.getLink());

        return newLink;
    }
}
