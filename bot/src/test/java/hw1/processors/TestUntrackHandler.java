package hw1.processors;

import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.processor.processors.UntrackHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.FAIL_UNTRACK_MESSAGE;
import static edu.java.bot.processor.Constants.SUCCESSFUL_UNTRACK_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class TestUntrackHandler {

    @Test
    public void testUnTrackFail() {
        // Setting mockUpdate
        var mockUpdate = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mockUpdate.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mockUpdate.getMessage().getText())
            .thenReturn("/untrack https://github.com/LZ https://github.com/LZ");

        // Setting mock scrapperClient
        var mockScrapperClient = Mockito.mock(ScrapperClient.class);

        // Assertions
        var method = new UntrackHandler(mockScrapperClient);
        assertThat(method.handle(mockUpdate)).isEqualTo(FAIL_UNTRACK_MESSAGE);
    }

    @Test
    public void testUnTrackSucessfull() {
        // Setting mockUpdate
        var mockUpdate = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mockUpdate.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mockUpdate.getMessage().getText()).thenReturn("/untrack https://github.com/LZ");

        // Setting mock scrapperClient
        var mockScrapperClient = Mockito.mock(ScrapperClient.class);

        // Assertions
        var method = new UntrackHandler(mockScrapperClient);
        assertThat(method.handle(mockUpdate)).isEqualTo(SUCCESSFUL_UNTRACK_MESSAGE);
    }
}
