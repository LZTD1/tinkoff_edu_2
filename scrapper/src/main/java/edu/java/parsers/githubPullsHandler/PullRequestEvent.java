package edu.java.parsers.githubPullsHandler;

import edu.java.clients.dto.githubDto.pull.PullDto;
import java.text.MessageFormat;
import static edu.java.parsers.web.WebGithub.SPEAKING_HEAD_EMOJI;

public class PullRequestEvent implements MessageHandler {
    private static String getReposName(PullDto entry) {
        return entry.getRepo().getUrl().getPath().split("/")[2];
    }

    @Override
    public String getMessage(PullDto entry) {
        if (entry.getPayload().getAction().equals("closed")) {
            return new StringBuilder()
                .append(SPEAKING_HEAD_EMOJI)
                .append(" Был произведен Merge PullRequest!\n")
                .append(MessageFormat.format(
                    "[{0}]({1})",
                    getReposName(entry),
                    entry.getRepo().getUrl().toString()
                ))
                .append(MessageFormat.format(
                    "Автор: [{0}]({1})",
                    entry.getActor().getLogin(),
                    entry.getActor().getUrl().toString()
                ))
                .append(MessageFormat.format(
                    "Название: {0}",
                    entry.getPayload().getPullRequest().getTitle()
                ))
                .append(MessageFormat.format(
                    "Мерж создал : [{0}]({1}), из ветки {2}\n",
                    entry.getPayload().getPullRequest().getMergedBy().getLogin(),
                    entry.getPayload().getPullRequest().getMergedBy().getUrl().toString(),
                    entry.getPayload().getPullRequest().getHead()
                ))
                .append(MessageFormat.format(
                    "Комментарии: {0}",
                    entry.getPayload().getPullRequest().getComments()
                ))
                .append(MessageFormat.format(
                    "Коммиты: {0}",
                    entry.getPayload().getPullRequest().getCommits()
                ))
                .append(MessageFormat.format(
                    "Ревью-комментарии: {0}",
                    entry.getPayload().getPullRequest().getReviewComments()
                ))
                .toString();
        }
        return null;
    }
}
