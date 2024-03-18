package edu.java.bot.dto;

import jakarta.validation.Valid;
import lombok.Data;
import java.net.URI;
import java.util.List;

@Data
public class LinkUpdate {

    private Long id;

    private URI url;

    private String description;

    @Valid
    private List<Long> tgChatIds;
}

