package edu.java.clients.dto.githubDto.commit.commit;

import lombok.Data;

@Data
public class CommitDto {

    private String message;

    private EntityDto author;

    private EntityDto committer;
}
