package edu.java.scrapperapi.services.jpa;

import edu.java.database.dto.User;
import edu.java.domain.jpa.UserRepository;
import edu.java.scrapperapi.exceptions.EntityAlreadyExistsError;
import edu.java.scrapperapi.exceptions.EntityDeleteException;
import edu.java.scrapperapi.services.TgChatService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

public class JpaTgChatService implements TgChatService {

    private UserRepository userRepository;

    public JpaTgChatService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(long tgChatId) {
        User user = new User();
        user.setTelegramId(tgChatId);
        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsError("Пользователь с таким telegramId уже существует!");
        }
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        var rowsAffected = userRepository.deleteByTelegramId(tgChatId);
        if (rowsAffected == 0) {
            throw new EntityDeleteException("Не возможно удалить юзера, возможно он уже был удален!");
        }
    }
}
