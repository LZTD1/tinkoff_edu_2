package hw1.processors;

import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.processor.processors.StartHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.START_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class TestStartHandler {

    @Test
    public void testStart() {
        // Setting mockUpdate
        var mockUpdate = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mockUpdate.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mockUpdate.getMessage().getChatId()).thenReturn(1L);

        // Setting mock scrapperClient
        var mockScrapperClient = Mockito.mock(ScrapperClient.class);

        // Assertions
        var method = new StartHandler(mockScrapperClient);
        assertThat(method.handle(mockUpdate)).isEqualTo(START_MESSAGE);
    }
}
