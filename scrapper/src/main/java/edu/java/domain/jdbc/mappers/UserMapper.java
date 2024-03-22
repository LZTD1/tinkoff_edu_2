package edu.java.domain.jdbc.mappers;

import edu.java.database.dto.User;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    @SneakyThrows
    public static User map(ResultSet rs, int rowNum) {
        User user = new User();

        user.setId(rs.getLong("id"));
        user.setTelegramId(rs.getLong("telegramid"));

        return user;
    }
}
