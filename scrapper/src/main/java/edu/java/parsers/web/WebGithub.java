package edu.java.parsers.web;

import edu.java.bot.dto.LinkUpdate;
import edu.java.clients.GithubClient;
import edu.java.clients.dto.githubDto.commit.CommitsDto;
import edu.java.clients.dto.githubDto.pull.PullDto;
import edu.java.database.dto.Link;
import edu.java.parsers.WebHandler;
import edu.java.scrapperapi.services.LinkService;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WebGithub implements WebHandler {

    private static final String SPEAKING_HEAD_EMOJI = "\uD83D\uDDE3";
    private static final int MAX_MESSAGE_LENGTH = 20;
    private final GithubClient githubClient;
    private final LinkService linkService;

    public WebGithub(GithubClient githubClient, LinkService linkService) {
        this.githubClient = githubClient;
        this.linkService = linkService;
    }

    @Override
    public String getHost() {
        return "github.com";
    }

    @Override
    public List<LinkUpdate> getUpdate(Link link) {
        List<CommitsDto> commitsDtoList = getGitCommits(link);
        List<PullDto> pullDtoList = getGitPulls(link);
        List<LinkUpdate> linkUpdateList = new ArrayList<>();

        OffsetDateTime lastCommitTime = gitCommitsProcessing(link, commitsDtoList, linkUpdateList);
        OffsetDateTime lastPullsTime = gitPullsProcessing(link, pullDtoList, linkUpdateList);

        OffsetDateTime newSendTime = lastCommitTime.isAfter(lastPullsTime) ? lastCommitTime : lastPullsTime;

        linkService.updateLastSendTime(link.getId(), newSendTime);

        return linkUpdateList;
    }

    private OffsetDateTime gitPullsProcessing(Link link, List<PullDto> pullDtoList, List<LinkUpdate> linkUpdateList) {
        OffsetDateTime newSendTime = link.getLastsendtime();
        for (PullDto entry : pullDtoList) {
            if (link.getLastsendtime().isBefore(entry.getCreatedAt())) {
                linkUpdateList.add(new LinkUpdate() {{
                    setDescription(getDescriptionMessage(entry));
                    setId(link.getId());
                    setUrl(link.getLink());
                    setTgChatIds(linkService.getAllUsersWithLink(link));
                }});

                if (newSendTime.isBefore(entry.getCreatedAt())) {
                    newSendTime = entry.getCreatedAt();
                }
            }
        }
        return newSendTime;
    }

    private OffsetDateTime gitCommitsProcessing(
        Link link,
        List<CommitsDto> commitsDtoList,
        List<LinkUpdate> linkUpdateList
    ) {
        OffsetDateTime newSendTime = link.getLastsendtime();
        for (CommitsDto entry : commitsDtoList) {
            if (link.getLastsendtime().isBefore(entry.getCommit().getCommitter().getDate())) {
                linkUpdateList.add(new LinkUpdate() {{
                    setDescription(getDescriptionMessage(entry));
                    setId(link.getId());
                    setUrl(link.getLink());
                    setTgChatIds(linkService.getAllUsersWithLink(link));
                }});
                if (newSendTime.isBefore(entry.getCommit().getCommitter().getDate())) {
                    newSendTime = entry.getCommit().getCommitter().getDate();
                }
            }
        }
        return newSendTime;
    }

    @SuppressWarnings("MultipleStringLiterals")
    private String getDescriptionMessage(CommitsDto entry) {
        return new StringBuilder()
            .append(SPEAKING_HEAD_EMOJI)
            .append("Нашел новый коммит!\n")
            .append(MessageFormat.format(
                "[{0}]({1})",
                getRepos(entry.getHtmlUrl().getPath()),
                entry.getHtmlUrl().toString()
            ))
            .append(MessageFormat.format(
                "Автор: [{0}]({1})",
                entry.getAuthor().getLogin(),
                entry.getAuthor().getHtmlUrl().toString()
            ))
            .append(MessageFormat.format(
                    "Сообщение: {0}",
                    trimMessage(entry.getCommit().getMessage())
                )
            )
            .toString();
    }

    private String getDescriptionMessage(PullDto entry) {
        return new StringBuilder()
            .append(SPEAKING_HEAD_EMOJI + " Нашел новый пулл!\n")
            .append(MessageFormat.format(
                "[{0}]({1})",
                getRepos(entry.getHtmlUrl().getPath()),
                entry.getHtmlUrl().toString()
            ))
            .append(MessageFormat.format(
                "Автор: [{0}]({1})",
                entry.getUser().getLogin(),
                entry.getUser().getHtmlUrl().toString()
            ))
            .append(MessageFormat.format(
                    "Сообщение: {0}",
                    trimMessage(entry.getBody())
                )
            )
            .toString();
    }

    private String trimMessage(String message) {
        return message.substring(0, Math.min(message.length(), MAX_MESSAGE_LENGTH)) + "...";
    }

    private List<CommitsDto> getGitCommits(Link link) {
        return githubClient.getCommitsByRepos(
            getOwner(link.getLink().getPath()),
            getRepos(link.getLink().getPath())
        ).collectList().block();
    }

    private List<PullDto> getGitPulls(Link link) {
        return githubClient.getPullsByRepos(
            getOwner(link.getLink().getPath()),
            getRepos(link.getLink().getPath())
        ).collectList().block();
    }

    private String getRepos(String path) {
        return path.split("/")[2];
    }

    private String getOwner(String path) {
        return path.split("/")[1];
    }
}
