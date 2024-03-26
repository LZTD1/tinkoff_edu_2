package edu.java.scrapperapi.services.jdbc;

import edu.java.domain.jdbc.UsersDao;
import edu.java.dto.User;
import edu.java.scrapperapi.exceptions.EntityAlreadyExistsError;
import edu.java.scrapperapi.exceptions.EntityDeleteException;
import edu.java.scrapperapi.services.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;

@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {

    private final UsersDao usersDao;

    @Override
    public void register(long tgChatId) {
        try {
            usersDao.createUser(new User() {{
                setTelegramId(tgChatId);
            }});
        } catch (DuplicateKeyException e) {
            throw new EntityAlreadyExistsError("Пользователь с таким telegramId уже существует!");
        }
    }

    @Override
    public void unregister(long tgChatId) {
        var rowsAffected = usersDao.deleteUser(new User() {{
            setTelegramId(tgChatId);
        }});
        if (rowsAffected == 0) {
            throw new EntityDeleteException("Не возможно удалить юзера, возможн он уже был удален!");
        }
    }
}
