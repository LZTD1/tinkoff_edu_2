package hw1.processors;

import edu.java.bot.processor.processors.DefaultHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.bot.processor.Constants.DEFAULT_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

class TestDefaultHandler {

    @Test
    public void testDefault() {
        var mock = Mockito.mock(Update.class);

        var method = new DefaultHandler();

        assertThat(method.handle(mock)).isEqualTo(DEFAULT_MESSAGE);
    }
}
