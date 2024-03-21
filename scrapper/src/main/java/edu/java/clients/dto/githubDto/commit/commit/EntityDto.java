package edu.java.clients.dto.githubDto.commit.commit;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class EntityDto {

    private String name;

    private String email;

    private OffsetDateTime date;
}
