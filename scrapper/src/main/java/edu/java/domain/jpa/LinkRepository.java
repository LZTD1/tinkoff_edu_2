package edu.java.domain.jpa;

import edu.java.database.dto.Link;
import java.net.URI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Link findLinkByLink(URI link);
}
