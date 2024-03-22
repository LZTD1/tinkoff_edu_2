package hw1.processors;

import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.processor.processors.ListHandler;
import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapper.dto.ListLinksResponse;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.EMPTY_LIST_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class TestListHandler {

    @Test
    public void testListEmpty() {
        // Setting mockUpdate
        var mockUpdate = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mockUpdate.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mockUpdate.getMessage().getChatId()).thenReturn(1L);

        // Creating empty ListLinksResponse
        ListLinksResponse emptyListLinks = new ListLinksResponse();
        emptyListLinks.setSize(0);
        emptyListLinks.setLinks(List.of());

        // Setting mock scrapperClient
        var mockScrapperClient = Mockito.mock(ScrapperClient.class);
        Mockito.when(mockScrapperClient.getAllTrackedLinks(
            Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()
        )).thenReturn(emptyListLinks);

        // Assertions
        var method = new ListHandler(mockScrapperClient);
        assertThat(method.handle(mockUpdate)).isEqualTo(EMPTY_LIST_MESSAGE);
    }

    @Test
    public void testListNotEmpty() {
        // Setting mockUpdate
        var mockUpdate = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mockUpdate.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mockUpdate.getMessage().getChatId()).thenReturn(1L);

        // Creating not-empty ListLinksResponse
        ListLinksResponse hasOneListLinks = new ListLinksResponse();
        hasOneListLinks.setSize(1);
        hasOneListLinks.setLinks(List.of(new LinkResponse() {{
            setId(1L);
            setUrl(URI.create("vk.com"));
        }}));

        // Setting mock scrapperClient
        var mockScrapperClient = Mockito.mock(ScrapperClient.class);
        Mockito.when(mockScrapperClient.getAllTrackedLinks(
            Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()
        )).thenReturn(hasOneListLinks);

        // Assertions
        var method = new ListHandler(mockScrapperClient);
        assertThat(method.handle(mockUpdate)).isEqualTo(convertToString(hasOneListLinks));
    }

    private String convertToString(ListLinksResponse hasOneListLinks) {
        return String.join(
            "\n",
            hasOneListLinks.getLinks().stream().map(e -> e.getUrl().toString()).toList()
        );
    }
}
