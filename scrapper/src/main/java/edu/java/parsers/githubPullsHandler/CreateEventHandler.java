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
            .append(SPEAKING_HEAD_EMOJI)
            .append(" Создана новая ветка!\n")
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
                    "Ветка {1} из {0}",
                    entry.getPayload().getMasterBranch(),
                    entry.getPayload().getRef()
                )
            )
            .toString();
    }
}
