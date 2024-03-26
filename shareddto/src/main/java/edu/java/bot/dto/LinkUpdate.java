package edu.java.bot.dto;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import lombok.Data;

@Data
public class LinkUpdate {

    private Long id;

    private URI url;

    private String description;

    @NotNull
    private List<Long> tgChatIds;
}

