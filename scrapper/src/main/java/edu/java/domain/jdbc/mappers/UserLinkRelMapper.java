package edu.java.domain.jdbc.mappers;

import edu.java.dto.Link;
import edu.java.dto.User;
import edu.java.dto.UserLinkRel;
import java.net.URI;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserLinkRelMapper {
    @SneakyThrows
    public static UserLinkRel map(ResultSet rs, int rowNum) {
        return new UserLinkRel() {{
            setLink(new Link() {{
                setLink(URI.create(rs.getObject("link", String.class)));
                setId(rs.getLong("linkid"));
            }});
            setUser(new User() {{
                setId(rs.getLong("userid"));
                setTelegramId(rs.getLong("telegramid"));
            }});
        }};
    }
}

