package hw1.processors;

import edu.java.database.SimpleDatabase;
import edu.java.processor.processors.TrackHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.database.SimpleDatabase.getInstance;
import static edu.java.processor.Constants.FAIL_TRACK_MESSAGE;
import static edu.java.processor.Constants.SUCCESSFUL_TRACK_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class TestTrackHandler {

    private static final SimpleDatabase db = getInstance();

    @BeforeEach
    void dropDb() {
        db.dropAll();
    }

    @Test
    public void testTrackFail() {
        var mock = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
        Mockito.when(mock.getMessage().getText()).thenReturn(
            new TrackHandler().getName() + " test test"
        );

        var method = new TrackHandler();
        assertThat(method.handle(mock)).isEqualTo(FAIL_TRACK_MESSAGE);
        assertThat(db.getUserLinksById(1L)).isEmpty();
    }

    @Test
    public void testTrackSucessfull() {
        var mock = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
        Mockito.when(mock.getMessage().getText()).thenReturn(
            new TrackHandler().getName() + " test"
        );

        var method = new TrackHandler();
        assertThat(method.handle(mock)).isEqualTo(SUCCESSFUL_TRACK_MESSAGE);
        assertThat(db.getUserLinksById(1L)).isNotEmpty();
    }
}
