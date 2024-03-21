//package hw1.processors;
//
//import edu.java.bot.processor.ProcessorHolder;
//import edu.java.bot.processor.processors.HelpHandler;
//import java.util.List;
//import java.util.stream.Collectors;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class TestHelpHandler {
//
//    @Test
//    public void testHelp() {
//        var mock = Mockito.mock(Update.class);
//        var mockProcessHolder = Mockito.mock(ProcessorHolder.class);
//
//
//        var helpMessage = getAllCommands()
//            .stream()
//            .map(entry -> entry.getName() + " - " + entry.getDescription() + " \n")
//            .collect(Collectors.joining());
//
//        var method = new HelpHandler();
//
//        assertThat(method.handle(mock)).isEqualTo(helpMessage);
//    }
//}
