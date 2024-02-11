package hw1;

import edu.java.processor.methods.Default;
import edu.java.processor.methods.Help;
import edu.java.processor.methods.List;
import edu.java.processor.methods.Start;
import edu.java.processor.methods.Track;
import edu.java.processor.methods.Untrack;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.database.SimpleDatabase.getInstance;
import static edu.java.processor.Constants.DEFAULT_MESSAGE;
import static edu.java.processor.Constants.EMPTY_LIST_MESSAGE;
import static edu.java.processor.Constants.FAIL_TRACK_MESSAGE;
import static edu.java.processor.Constants.FAIL_UNTRACK_MESSAGE;
import static edu.java.processor.Constants.HELP_MESSAGE;
import static edu.java.processor.Constants.START_MESSAGE;
import static edu.java.processor.Constants.SUCCESSFUL_TRACK_MESSAGE;
import static edu.java.processor.Constants.SUCCESSFUL_UNTRACK_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class TestProcessors {

    @Test
    public void testDefault() {
        var mock = Mockito.mock(Update.class);

        var method = new Default();

        assertThat(method.get(mock)).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    public void testHelp() {
        var mock = Mockito.mock(Update.class);

        var method = new Help();

        assertThat(method.get(mock)).isEqualTo(HELP_MESSAGE);
    }

    @Test
    public void testStart() {
        var mock = Mockito.mock(Update.class);

        var method = new Start();

        assertThat(method.get(mock)).isEqualTo(START_MESSAGE);
    }

    @Test
    public void testList_Empty() {
        var mock = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);

        Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);

        var method = new List();

        assertThat(method.get(mock)).isEqualTo(EMPTY_LIST_MESSAGE);
    }

    @Test
    public void testList_NotEmpty() {
        var link = "google.com";
        var db = getInstance();
        db.registerUser(1L);
        db.addLink(1L, link);

        var mock = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);

        var method = new List();
        assertThat(method.get(mock)).isEqualTo(link);
        db.dropAll();
    }

    @Test
    public void testTrack_Fail() {
        var mock = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
        Mockito.when(mock.getMessage().getText()).thenReturn(
            new Track().getName() + " test test"
        );

        var method = new Track();
        assertThat(method.get(mock)).isEqualTo(FAIL_TRACK_MESSAGE);
    }

    @Test
    public void testTrack_Sucessfull() {
        var mock = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
        Mockito.when(mock.getMessage().getText()).thenReturn(
            new Track().getName() + " test"
        );

        var method = new Track();
        assertThat(method.get(mock)).isEqualTo(SUCCESSFUL_TRACK_MESSAGE);
    }

    @Test
    public void testUnTrack_Fail() {
        var mock = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
        Mockito.when(mock.getMessage().getText()).thenReturn(
            new Untrack().getName() + " test test"
        );

        var method = new Untrack();
        assertThat(method.get(mock)).isEqualTo(FAIL_UNTRACK_MESSAGE);
    }

    @Test
    public void testUnTrack_Sucessfull() {
        var mock = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
        Mockito.when(mock.getMessage().getText()).thenReturn(
            new Untrack().getName() + " test"
        );

        var method = new Untrack();
        assertThat(method.get(mock)).isEqualTo(SUCCESSFUL_UNTRACK_MESSAGE);
    }
}
