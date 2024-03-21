package edu.java.bot.processor.processors;

import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.processor.MethodProcessor;
import java.net.URI;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.FAIL_TRACK_MESSAGE;
import static edu.java.bot.processor.Constants.SUCCESSFUL_TRACK_MESSAGE;

@Component
public class TrackHandler implements MethodProcessor {

    private ScrapperClient scrapperClient;

    public TrackHandler(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String handle(Update update) {
        String[] param = update.getMessage().getText().split(" ");
        if (param.length == 2) {
            scrapperClient.addTrackLink(update.getMessage().getChatId(), URI.create(param[1]));
            return SUCCESSFUL_TRACK_MESSAGE;
        } else {
            return FAIL_TRACK_MESSAGE;
        }
    }

    @Override
    public String getDescription() {
        return "начать отслеживание ссылки";
    }

    @Override
    public String getName() {
        return "/track";
    }
}
