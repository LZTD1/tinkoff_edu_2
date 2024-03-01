package hw1.processors;

import edu.java.bot.processor.processors.UntrackHandler;
import edu.java.database.SimpleDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.FAIL_UNTRACK_MESSAGE;
import static edu.java.bot.processor.Constants.SUCCESSFUL_UNTRACK_MESSAGE;
import static edu.java.database.SimpleDatabase.getInstance;
import static org.assertj.core.api.Assertions.assertThat;

public class TestUntrackHandler {

    private static final SimpleDatabase db = getInstance();

    @BeforeEach
    void dropDb() {
        db.dropAll();
    }

    @Test
    public void testUnTrackFail() {
        var mock = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
        Mockito.when(mock.getMessage().getText()).thenReturn(
            new UntrackHandler().getName() + " test test"
        );

        var method = new UntrackHandler();
        assertThat(method.handle(mock)).isEqualTo(FAIL_UNTRACK_MESSAGE);
        assertThat(db.getUserLinksById(1L)).isEmpty();
    }

    @Test
    public void testUnTrackSucessfull() {
        var mock = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
        Mockito.when(mock.getMessage().getText()).thenReturn(
            new UntrackHandler().getName() + " test"
        );

        var method = new UntrackHandler();
        assertThat(method.handle(mock)).isEqualTo(SUCCESSFUL_UNTRACK_MESSAGE);
        assertThat(db.getUserLinksById(1L)).isEmpty();

    }
}
