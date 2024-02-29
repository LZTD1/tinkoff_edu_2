package hw1.processors;

import edu.java.database.SimpleDatabase;
import edu.java.bot.processor.processors.ListHandler;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.EMPTY_LIST_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

public class TestListHandler {

    @Test
    public void testListEmpty() {
        try (MockedStatic<SimpleDatabase> utilities = Mockito.mockStatic(SimpleDatabase.class)) {
            SimpleDatabase mockInstance = Mockito.mock(SimpleDatabase.class);
            utilities.when(SimpleDatabase::getInstance).thenReturn(mockInstance);

            Mockito.when(mockInstance.getUserLinksById(Mockito.anyLong())).thenReturn(Collections.emptyList());

            var mock = Mockito.mock(Update.class);
            var mockedMessage = Mockito.mock(Message.class);
            Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
            Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);
            var method = new ListHandler();
            assertThat(method.handle(mock)).isEqualTo(EMPTY_LIST_MESSAGE);
        }
    }

    @Test
    public void testListNotEmpty() {
        try (MockedStatic<SimpleDatabase> utilities = Mockito.mockStatic(SimpleDatabase.class)) {
            SimpleDatabase mockInstance = Mockito.mock(SimpleDatabase.class);
            utilities.when(SimpleDatabase::getInstance).thenReturn(mockInstance);
            var link = "google.com";

            Mockito.when(mockInstance.getUserLinksById(Mockito.anyLong())).thenReturn(new ArrayList<>() {{
                add(link);
            }});

            var mock = Mockito.mock(Update.class);
            var mockedMessage = Mockito.mock(Message.class);
            Mockito.when(mock.getMessage()).thenReturn(mockedMessage);
            Mockito.when(mock.getMessage().getChatId()).thenReturn(1L);

            var method = new ListHandler();
            assertThat(method.handle(mock)).isEqualTo(link);
        }
    }
}
