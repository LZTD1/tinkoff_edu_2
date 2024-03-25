package edu.java.database.dto;

import edu.java.database.dto.converters.UriConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.net.URI;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@ToString

@Table(
    name = "links",
    uniqueConstraints = @UniqueConstraint(columnNames = "link")
)
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    @Convert(converter = UriConverter.class)
    private URI link;

    @Column(nullable = false)
    private OffsetDateTime updatetime = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime lastsendtime = OffsetDateTime.now();
}
