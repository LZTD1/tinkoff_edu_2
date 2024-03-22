package hw1.processors;

import edu.java.bot.processor.MethodProcessor;
import edu.java.bot.processor.ProcessorHolder;
import edu.java.bot.processor.processors.HelpHandler;
import edu.java.bot.processor.processors.StartHandler;
import edu.java.bot.processor.processors.UntrackHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.assertj.core.api.Assertions.assertThat;

public class TestHelpHandler {

    @Test
    public void testHelp() {
        List<MethodProcessor> allCommands = new ArrayList<>() {{
            add(new HelpHandler(null));
            add(new StartHandler(null));
            add(new UntrackHandler(null));
        }};

        var update = Mockito.mock(Update.class);
        var mockProcessHolder = Mockito.mock(ProcessorHolder.class);

        Mockito.when(mockProcessHolder.getAllCommands()).thenReturn(
            allCommands
        );

        var helpMessage = mockProcessHolder.getAllCommands()
            .stream()
            .map(entry -> entry.getName() + " - " + entry.getDescription() + " \n")
            .collect(Collectors.joining());

        var method = new HelpHandler(allCommands);
        assertThat(method.handle(update)).isEqualTo(helpMessage);
    }
}
