package hw1.processors;

import edu.java.database.SimpleDatabase;
import edu.java.bot.processor.processors.HelpHandler;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.database.SimpleDatabase.getInstance;
import static edu.java.bot.processor.ProcessorHolder.getAllCommands;
import static org.assertj.core.api.Assertions.assertThat;

public class TestHelpHandler {

    private static final SimpleDatabase db = getInstance();

    @BeforeAll
    static void dropDb() {
        db.dropAll();
    }

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
