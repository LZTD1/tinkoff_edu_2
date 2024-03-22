package edu.java.domain.jdbc.mappers;

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
}
