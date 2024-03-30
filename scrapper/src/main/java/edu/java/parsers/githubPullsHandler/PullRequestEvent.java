package edu.java.parsers.githubPullsHandler;

import edu.java.clients.dto.githubDto.pull.PullDto;
import java.text.MessageFormat;
import static edu.java.parsers.web.WebGithub.SPEAKING_HEAD_EMOJI;

public class PullRequestEvent implements MessageHandler {
    private static final int INDEX_REPOS_NAME = 3;

    private static String getReposName(PullDto entry) {
        return entry.getRepo().getUrl().getPath().split("/")[INDEX_REPOS_NAME];
    }

    @Override
    public String getMessage(PullDto entry) {
        if (entry.getPayload().getAction().equals("closed")) {
            return new StringBuilder()
                .append(SPEAKING_HEAD_EMOJI).append("Был произведен Merge PullRequest!\n")
                .append("\n")
                .append(MessageFormat.format(
                    "<b>Репозиторий:</b> <a href=''{0}''>{1}</a>",
                    entry.getRepo().getUrl().toString(),
                    getReposName(entry)
                ))
                .append("\n")
                .append(MessageFormat.format(
                    "<b>Автор:</b> <a href=''{0}''>{1}</a>",
                    entry.getActor().getUrl().toString(),
                    entry.getActor().getLogin()
                ))
                .append("\n")
                .append(MessageFormat.format(
                    "<b>Название мержа:</b> {0}",
                    entry.getPayload().getPullRequest().getTitle()
                ))
                .append("\n")
                .append(MessageFormat.format(
                    "<b>Мерж создал:</b> <a href=''{0}''>{1}</a>",
                    entry.getPayload().getPullRequest().getMergedBy().getUrl().toString(),
                    entry.getPayload().getPullRequest().getMergedBy().getLogin()
                ))
                .append("\n")
                .append(MessageFormat.format(
                    "<b>Из ветки:</b> {0}\n",
                    entry.getPayload().getPullRequest().getHead().getRef()
                ))
                .append("\n")
                .append(MessageFormat.format(
                    "<b>Комментарии:</b> {0}",
                    entry.getPayload().getPullRequest().getComments()
                ))
                .append("\n")
                .append(MessageFormat.format(
                    "<b>Коммиты:</b> {0}",
                    entry.getPayload().getPullRequest().getCommits()
                ))
                .append("\n")
                .append(MessageFormat.format(
                    "<b>Ревью-комментарии:</b> {0}",
                    entry.getPayload().getPullRequest().getReviewComments()
                ))
                .toString();
        }
        return null;
    }
}
