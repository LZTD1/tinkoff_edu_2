package edu.java.bot.processor.processors;

import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.processor.MethodProcessor;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.FAIL_UNTRACK_MESSAGE;
import static edu.java.bot.processor.Constants.SUCCESSFUL_UNTRACK_MESSAGE;

@Component
@RequiredArgsConstructor
public class UntrackHandler implements MethodProcessor {

    private final ScrapperClient scrapperClient;

    @Override
    public String handle(Update update) {
        String[] param = update.getMessage().getText().split(" ");

        if (param.length != 2) {
            return FAIL_UNTRACK_MESSAGE;
        }

        scrapperClient.deleteTrackLink(update.getMessage().getChatId(), URI.create(param[1]));
        return SUCCESSFUL_UNTRACK_MESSAGE;
    }

    @Override
    public String getDescription() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    public String getName() {
        return "/untrack";
    }
}
