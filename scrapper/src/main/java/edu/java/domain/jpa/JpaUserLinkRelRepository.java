package edu.java.domain.jpa;

import edu.java.database.dto.UserLinkRel;
import edu.java.database.dto.UsersLinkId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings({"MethodName", "ParameterName"})
public interface JpaUserLinkRelRepository extends JpaRepository<UserLinkRel, UsersLinkId> {
    List<UserLinkRel> findAllByUserid_TelegramId(Long userid_telegramId);

    List<UserLinkRel> findByLinkid_Id(Long id);
}
