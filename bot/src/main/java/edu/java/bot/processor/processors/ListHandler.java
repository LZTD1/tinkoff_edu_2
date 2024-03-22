package edu.java.bot.processor.processors;

import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.processor.MethodProcessor;
import edu.java.scrapper.dto.ListLinksResponse;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.EMPTY_LIST_MESSAGE;

@Component
public class ListHandler implements MethodProcessor {

    private ScrapperClient scrapperClient;

    public ListHandler(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    private static String convertToString(ListLinksResponse links) {
        return String.join(
            "\n",
            links.getLinks().stream().map(e -> e.getUrl().toString()).toList()
        );
    }

    @Override
    public String handle(Update update) {
        ListLinksResponse links = scrapperClient.getAllTrackedLinks(
            update.getMessage().getChatId(),
            25,
            0
        );
        // todo реализовать пагинацию

        if (links.getLinks().isEmpty()) {
            return EMPTY_LIST_MESSAGE;
        }

        return convertToString(links);
    }

    @Override
    public String getDescription() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    public String getName() {
        return "/list";
    }
}
