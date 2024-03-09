package hw1.processors;

import edu.java.bot.processor.processors.StartHandler;
import edu.java.database.SimpleDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.START_MESSAGE;
import static edu.java.database.SimpleDatabase.getInstance;
import static org.assertj.core.api.Assertions.assertThat;

public class TestStartHandler {

    private static final SimpleDatabase db = getInstance();

    @BeforeAll
    static void dropDb() {
        db.dropAll();
    }

    @Test
    public void testStart() {
        var mock = Mockito.mock(Update.class);

        var method = new StartHandler();

        assertThat(method.handle(mock)).isEqualTo(START_MESSAGE);
    }
}
