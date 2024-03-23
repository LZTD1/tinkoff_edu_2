package edu.java.database.dto;

import edu.java.database.dto.converters.UriConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
