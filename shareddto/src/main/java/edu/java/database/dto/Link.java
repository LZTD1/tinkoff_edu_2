package edu.java.database.dto;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class Link {

    private Long id;

    private URI link;

    private OffsetDateTime updatetime;

    private OffsetDateTime lastsendtime;
}
