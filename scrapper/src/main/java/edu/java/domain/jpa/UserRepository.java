package edu.java.domain.jpa;

import edu.java.database.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    long deleteByTelegramId(Long telegramId);

    User getUserByTelegramId(Long telegramId);
}
