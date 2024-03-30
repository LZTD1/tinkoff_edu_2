package edu.java.domain.jdbc.mappers;

import edu.java.dto.User;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jooq.Record;
import static edu.java.domain.jooq.tables.Tables.USERS;

@UtilityClass
public class UserMapper {
    @SneakyThrows
    public static User map(ResultSet rs, int rowNum) {
        User user = new User();

        user.setId(rs.getLong("id"));
        user.setTelegramId(rs.getLong("telegramid"));

        return user;
    }

    @SneakyThrows
    public static User mapFromRecord(Record r) {
        User user = new User();

        user.setId(r.get(USERS.ID));
        user.setTelegramId(r.get(USERS.TELEGRAMID));

        return user;
    }
}
