package edu.java.parsers.githubPullsHandler;

import edu.java.clients.dto.githubDto.pull.PullDto;
import java.text.MessageFormat;
import static edu.java.parsers.web.WebGithub.SPEAKING_HEAD_EMOJI;

public class CreateEventHandler implements MessageHandler {
    private static String getReposName(PullDto entry) {
        return entry.getRepo().getUrl().getPath().split("/")[2];
    }

    @Override
    public String getMessage(PullDto entry) {
        return new StringBuilder()
            .append(SPEAKING_HEAD_EMOJI).append("Создана новая ветка!")
            .append("\n\n")
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
                    "<b>Новая ветка:</b> {0}, из {1}",
                    entry.getPayload().getRef(),
                    entry.getPayload().getMasterBranch()
                )
            )
            .toString();
    }
}
