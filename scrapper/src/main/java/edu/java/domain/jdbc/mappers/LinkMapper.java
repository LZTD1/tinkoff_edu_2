package edu.java.domain.jdbc.mappers;

import edu.java.database.dto.Link;
import java.net.URI;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jooq.Record;
import static edu.java.domain.jooq.tables.Tables.LINKS;

@UtilityClass
public class LinkMapper {
    @SneakyThrows
    public static Link map(ResultSet rs, int rowNum) {
        Link link = new Link();

        link.setId(rs.getLong("id"));
        link.setLink(URI.create(rs.getObject("link", String.class)));
        link.setUpdatetime(rs.getObject("updatetime", OffsetDateTime.class));
        link.setLastsendtime(rs.getObject("lastsendtime", OffsetDateTime.class));

        return link;
    }

    @SneakyThrows
    public static Link mapFromRecord(Record entity) {
        Link link = new Link();

        link.setId(entity.get(LINKS.ID));
        link.setLink(URI.create(entity.get(LINKS.LINK)));
        link.setUpdatetime(entity.get(LINKS.UPDATETIME));
        link.setLastsendtime(entity.get(LINKS.LASTSENDTIME));

        return link;
    }
}
