package hw1;

import edu.java.database.SimpleDatabase;
import edu.java.processor.processors.DefaultHandler;
import edu.java.processor.processors.HelpHandler;
import edu.java.processor.processors.ListHandler;
import edu.java.processor.processors.StartHandler;
import edu.java.processor.processors.TrackHandler;
import edu.java.processor.processors.UntrackHandler;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.database.SimpleDatabase.getInstance;
import static edu.java.processor.Constants.DEFAULT_MESSAGE;
import static edu.java.processor.Constants.EMPTY_LIST_MESSAGE;
import static edu.java.processor.Constants.FAIL_TRACK_MESSAGE;
import static edu.java.processor.Constants.FAIL_UNTRACK_MESSAGE;
import static edu.java.processor.Constants.START_MESSAGE;
import static edu.java.processor.Constants.SUCCESSFUL_TRACK_MESSAGE;
import static edu.java.processor.Constants.SUCCESSFUL_UNTRACK_MESSAGE;
import static edu.java.processor.ProcessorHolder.getAllCommands;
import static org.assertj.core.api.Assertions.assertThat;

public class TestProcessors {
    private final SimpleDatabase db = getInstance();

    @BeforeEach
    void dropDb() {
        db.dropAll();
    }

    @Nested
    class TestDefaultHandler {
        @Test
        public void testDefault() {
            var mock = Mockito.mock(Update.class);

            var method = new DefaultHandler();

            assertThat(method.handle(mock)).isEqualTo(DEFAULT_MESSAGE);
        }
    }

    @Nested
    class TestHelpHandler {
        @Test
        public void testHelp() {
            var mock = Mockito.mock(Update.class);
            var helpMessage = getAllCommands()
                .stream()
                .map(entry -> entry.getName() + " - " + entry.getDescription() + " \n")
                .collect(Collectors.joining());

            var method = new HelpHandler();

            assertThat(method.handle(mock)).isEqualTo(helpMessage);
        }
    }

    @Nested
    class testStartHandler {
        @Test
        public void testStart() {
            var mock = Mockito.mock(Update.class);

            var method = new StartHandler();

            assertThat(method.handle(mock)).isEqualTo(START_MESSAGE);
        }
    }

    @Nested
    class testListHandler {

        @Test
        public void testList_Empty() {
            var mock = Mockito.mock(Update.class);
            var mockedMessage = Mockito.mock(Message.class);

            Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
            Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);

            var method = new ListHandler();

            assertThat(method.handle(mock)).isEqualTo(EMPTY_LIST_MESSAGE);
        }

        @Test
        public void testList_NotEmpty() {
            var link = "google.com";

            db.registerUser(1L);
            db.addLink(1L, link);

            var mock = Mockito.mock(Update.class);
            var mockedMessage = Mockito.mock(Message.class);
            Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
            Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);

            var method = new ListHandler();
            assertThat(method.handle(mock)).isEqualTo(link);
        }
    }

    @Nested
    class testTrackHandler {

        @Test
        public void testTrack_Fail() {
            var mock = Mockito.mock(Update.class);
            var mockedMessage = Mockito.mock(Message.class);
            Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
            Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
            Mockito.when(mock.getMessage().getText()).thenReturn(
                new TrackHandler().getName() + " test test"
            );

            var method = new TrackHandler();
            assertThat(method.handle(mock)).isEqualTo(FAIL_TRACK_MESSAGE);
        }

        @Test
        public void testTrack_Sucessfull() {
            var mock = Mockito.mock(Update.class);
            var mockedMessage = Mockito.mock(Message.class);
            Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
            Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
            Mockito.when(mock.getMessage().getText()).thenReturn(
                new TrackHandler().getName() + " test"
            );

            var method = new TrackHandler();
            assertThat(method.handle(mock)).isEqualTo(SUCCESSFUL_TRACK_MESSAGE);
        }

    }

    @Nested
    class testUntrackHandler {
        @Test
        public void testUnTrack_Fail() {
            var mock = Mockito.mock(Update.class);
            var mockedMessage = Mockito.mock(Message.class);
            Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
            Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
            Mockito.when(mock.getMessage().getText()).thenReturn(
                new UntrackHandler().getName() + " test test"
            );

            var method = new UntrackHandler();
            assertThat(method.handle(mock)).isEqualTo(FAIL_UNTRACK_MESSAGE);
        }

        @Test
        public void testUnTrack_Sucessfull() {
            var mock = Mockito.mock(Update.class);
            var mockedMessage = Mockito.mock(Message.class);
            Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
            Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
            Mockito.when(mock.getMessage().getText()).thenReturn(
                new UntrackHandler().getName() + " test"
            );

            var method = new UntrackHandler();
            assertThat(method.handle(mock)).isEqualTo(SUCCESSFUL_UNTRACK_MESSAGE);
        }
    }

}
