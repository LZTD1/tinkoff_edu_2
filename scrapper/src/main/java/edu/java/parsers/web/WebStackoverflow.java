package edu.java.parsers.web;

import edu.java.bot.dto.LinkUpdate;
import edu.java.clients.StackoverflowClient;
import edu.java.clients.dto.sofDto.ItemsDto;
import edu.java.clients.dto.sofDto.StackOverFlowDto;
import edu.java.database.dto.Link;
import edu.java.parsers.WebHandler;
import edu.java.scrapperapi.services.LinkService;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class WebStackoverflow implements WebHandler {

    private static final String EYES_EMOJI = "\uD83D\uDC40";
    private static final String CUP_EMOJI = "\uD83C\uDFC6";
    private final LinkService linkService;
    private final StackoverflowClient stackoverflowClient;
    private static final int MAX_MESSAGE_LENGTH = 20;
    private OffsetDateTime newSendTime;

    @Getter
    enum Types {
        ANSWER("ответ"),
        COMMENT("комментарий");

        private final String name;

        Types(String text) {
            this.name = text;
        }
    }

    public WebStackoverflow(LinkService linkService, StackoverflowClient stackoverflowClient) {
        this.linkService = linkService;
        this.stackoverflowClient = stackoverflowClient;
    }

    public String getHost() {
        return "stackoverflow.com";
    }

    @Override
    public List<LinkUpdate> getUpdate(Link link) {
        StackOverFlowDto sofComments = getSofComments(link);
        StackOverFlowDto sofAnswers = getSofAnswers(link);
        List<LinkUpdate> linkUpdateList = new ArrayList<>();

        newSendTime = link.getLastsendtime();

        sofCommentsProcessing(link, sofComments, linkUpdateList);
        sofAnswersProcessing(link, sofAnswers, linkUpdateList);

        linkService.updateLastSendTime(link.getId(), newSendTime);

        return linkUpdateList;
    }

    private void sofAnswersProcessing(Link link, StackOverFlowDto sofAnswers, List<LinkUpdate> linkUpdateList) {
        sofAnswers.getItems().forEach(entry -> {
            if (link.getLastsendtime().isBefore(entry.getCreationDate())) {
                linkUpdateList.add(
                    new LinkUpdate() {{
                        setDescription(getDescriptionMessage(entry, Types.ANSWER, link));
                        setId(link.getId());
                        setUrl(link.getLink());
                        setTgChatIds(linkService.getAllUsersWithLink(link));
                    }}
                );

                if (newSendTime.isBefore(entry.getCreationDate())) {
                    newSendTime = entry.getCreationDate();
                }
            }
        });
    }

    private void sofCommentsProcessing(Link link, StackOverFlowDto sofComments, List<LinkUpdate> linkUpdateList) {
        sofComments.getItems().forEach(entry -> {
            if (link.getLastsendtime().isBefore(entry.getCreationDate())) {
                linkUpdateList.add(
                    new LinkUpdate() {{
                        setDescription(getDescriptionMessage(entry, Types.COMMENT, link));
                        setId(link.getId());
                        setUrl(link.getLink());
                        setTgChatIds(linkService.getAllUsersWithLink(link));
                    }}
                );

                if (newSendTime.isBefore(entry.getCreationDate())) {
                    newSendTime = entry.getCreationDate();
                }
            }
        });
    }

    private String getDescriptionMessage(ItemsDto entry, Types comment, Link link) {
        return new StringBuilder()
            .append(EYES_EMOJI + "Нашел новый " + comment.getName() + "\n")
            .append(MessageFormat.format(
                "[link]({0})",
                link.getLink().toString()
            ))
            .append(MessageFormat.format(
                "Автор: [{0}]({1}) {2} " + CUP_EMOJI,
                entry.getOwner().getDisplayName(),
                entry.getOwner().getLink(),
                entry.getOwner().getReputation()
            ))
            .append(MessageFormat.format(
                    "Сообщение: {0}",
                    trimMessage(entry.getBody())
                )
            )
            .toString();
    }

    private Object trimMessage(String body) {
        return body.substring(0, Math.min(body.length(), MAX_MESSAGE_LENGTH)) + "...";
    }

    @Nullable private StackOverFlowDto getSofAnswers(Link link) {
        return stackoverflowClient.getAnswersByQuestion(
            Integer.parseInt(getId(link.getLink().getPath()))
        ).block();
    }

    @Nullable private StackOverFlowDto getSofComments(Link link) {
        return stackoverflowClient.getCommentsByQuestion(
            Integer.parseInt(getId(link.getLink().getPath()))
        ).block();
    }

    private String getId(String path) {
        return path.split("/")[2];
    }
}
