package edu.java.scrapperapi.services.jpa;

import edu.java.database.dto.User;
import edu.java.domain.jpa.JpaUserRepository;
import edu.java.scrapperapi.exceptions.EntityAlreadyExistsError;
import edu.java.scrapperapi.exceptions.EntityDeleteException;
import edu.java.scrapperapi.services.TgChatService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

public class JpaTgChatService implements TgChatService {

    private JpaUserRepository jpaUserRepository;

    public JpaTgChatService(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void register(long tgChatId) {
        User user = new User();
        user.setTelegramId(tgChatId);
        try {
            jpaUserRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsError("Пользователь с таким telegramId уже существует!");
        }
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        var rowsAffected = jpaUserRepository.deleteByTelegramId(tgChatId);
        if (rowsAffected == 0) {
            throw new EntityDeleteException("Не возможно удалить юзера, возможно он уже был удален!");
        }
    }
}
