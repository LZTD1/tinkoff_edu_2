package edu.java.domain.jpa;

import edu.java.database.dto.UserLinkRel;
import edu.java.database.dto.UsersLinkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLinkRelRepository extends JpaRepository<UserLinkRel, UsersLinkId> {
}
