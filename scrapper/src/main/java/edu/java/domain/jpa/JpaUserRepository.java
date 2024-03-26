package edu.java.domain.jpa;

import edu.java.database.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {

    long deleteByTelegramId(Long telegramId);

    User getUserByTelegramId(Long telegramId);
}
