package hw1.processors;

import edu.java.database.SimpleDatabase;
import edu.java.processor.processors.DefaultHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.database.SimpleDatabase.getInstance;
import static edu.java.processor.Constants.DEFAULT_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

class TestDefaultHandler {

    private static final SimpleDatabase db = getInstance();

    @BeforeAll
    static void dropDb() {
        db.dropAll();
    }

    @Test
    public void testDefault() {
        var mock = Mockito.mock(Update.class);

        var method = new DefaultHandler();

        assertThat(method.handle(mock)).isEqualTo(DEFAULT_MESSAGE);
    }
}
