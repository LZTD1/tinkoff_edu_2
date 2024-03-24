package edu.java.scrapperapi.services.jooq;

import edu.java.database.dto.User;
import edu.java.domain.jooq.JooqUserRepository;
import edu.java.scrapperapi.exceptions.EntityAlreadyExistsError;
import edu.java.scrapperapi.exceptions.EntityDeleteException;
import edu.java.scrapperapi.services.TgChatService;
import org.springframework.dao.DuplicateKeyException;

public class JooqTgChatService implements TgChatService {

    private JooqUserRepository jooqUserRepository;

    public JooqTgChatService(JooqUserRepository jooqUserRepository) {
        this.jooqUserRepository = jooqUserRepository;
    }

    @Override
    public void register(long tgChatId) {
        try {
            jooqUserRepository.createUser(new User() {{
                setTelegramId(tgChatId);
            }});
        } catch (
            DuplicateKeyException e) {
            throw new EntityAlreadyExistsError("Пользователь с таким telegramId уже существует!");
        }
    }

    @Override
    public void unregister(long tgChatId) {
        var rowsAffected = jooqUserRepository.deleteUser(new User() {{
            setTelegramId(tgChatId);
        }});
        if (rowsAffected == 0) {
            throw new EntityDeleteException("Не возможно удалить юзера, возможн он уже был удален!");
        }
    }
}
