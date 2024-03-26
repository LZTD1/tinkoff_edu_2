package edu.java.domain.jpa;

import edu.java.dto.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    Link findLinkByLink(URI link);

    @Query(
        "SELECT l FROM Link l WHERE current_timestamp - l.updatetime > :min"
    )
    List<Link> getLinksNotUpdates(@Param("min") Duration minutes);

    @Transactional
    @Modifying
    @Query("update Link l set l.lastsendtime = ?1, l.updatetime = current_timestamp where l.id = ?2")
    void updateLastsendtimeById(OffsetDateTime lastsendtime, Long id);

}
