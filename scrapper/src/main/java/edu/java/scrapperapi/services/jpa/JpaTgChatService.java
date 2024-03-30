package edu.java.scrapperapi.services.jpa;

import edu.java.domain.jpa.JpaUserRepository;
import edu.java.dto.User;
import edu.java.scrapperapi.exceptions.EntityAlreadyExistsError;
import edu.java.scrapperapi.exceptions.EntityDeleteException;
import edu.java.scrapperapi.services.TgChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {

    private final JpaUserRepository jpaUserRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        User user = new User();
        user.setTelegramId(tgChatId);
        try {
            jpaUserRepository.save(user);
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
