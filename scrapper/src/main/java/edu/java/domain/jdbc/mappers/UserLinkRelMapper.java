package edu.java.domain.jdbc.mappers;

import edu.java.database.dto.Link;
import edu.java.database.dto.User;
import java.net.URI;
import java.sql.ResultSet;
import edu.java.database.dto.UserLinkRel;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserLinkRelMapper {
    @SneakyThrows
    public static UserLinkRel map(ResultSet rs, int rowNum) {
        return new UserLinkRel() {{
            setLinkid(new Link() {{
                setLink(URI.create(rs.getObject("link", String.class)));
                setId(rs.getLong("linkid"));
            }});
            setUserid(new User() {{
                setId(rs.getLong("userid"));
                setTelegramId(rs.getLong("telegramid"));
            }});
        }};
    }
}

