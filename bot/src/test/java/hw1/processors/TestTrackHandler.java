package hw1.processors;

import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.processor.linkValidator.GithubValidator;
import edu.java.bot.processor.linkValidator.LinkValidator;
import edu.java.bot.processor.linkValidator.StackoverflowValidator;
import edu.java.bot.processor.processors.TrackHandler;

import java.text.MessageFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static edu.java.bot.processor.Constants.FAIL_TRACK_MESSAGE;
import static edu.java.bot.processor.Constants.INCORRECT_LINK_TYPE;
import static edu.java.bot.processor.Constants.SUCCESSFUL_TRACK_MESSAGE;
import static edu.java.bot.processor.Constants.UNSUPPORTED_TRACK_LINK;
import static org.assertj.core.api.Assertions.assertThat;

public class TestTrackHandler {
    @Test
    public void testTrackFail_Length() {
        // Setting mockUpdate
        var mockUpdate = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mockUpdate.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mockUpdate.getMessage().getText()).thenReturn("/track hello test");

        // Setting mock scrapperClient
        var mockScrapperClient = Mockito.mock(ScrapperClient.class);

        // Assertions
        var method = new TrackHandler(mockScrapperClient, List.of());
        assertThat(method.handle(mockUpdate)).isEqualTo(FAIL_TRACK_MESSAGE);
    }

    @Test
    public void testTrackFail_UnsupportedTrackLink() {
        // Setting mockUpdate
        var mockUpdate = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mockUpdate.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mockUpdate.getMessage().getText()).thenReturn("/track vk.com");

        // Setting mock scrapperClient
        var mockScrapperClient = Mockito.mock(ScrapperClient.class);

        // Creating args
        var validators = List.of(
            new GithubValidator(),
            new StackoverflowValidator()
        );

        // Assertions
        var method = new TrackHandler(mockScrapperClient, validators);
        assertThat(method.handle(mockUpdate)).isEqualTo(
            MessageFormat.format("{0}\n{1}", UNSUPPORTED_TRACK_LINK, getSupportedResources(validators)));
    }

    @Test
    public void testTrackFail_INCORRECT_LINK_TYPE() {
        // Setting mockUpdate
        var mockUpdate = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mockUpdate.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mockUpdate.getMessage().getText()).thenReturn("/track https://github.com/LZ");

        // Setting mock scrapperClient
        var mockScrapperClient = Mockito.mock(ScrapperClient.class);

        // Creating args
        var validators = List.of(
            new GithubValidator(),
            new StackoverflowValidator()
        );

        // Assertions
        var method = new TrackHandler(mockScrapperClient, validators);
        assertThat(method.handle(mockUpdate)).isEqualTo(MessageFormat.format(
            "{0}{1}",
            INCORRECT_LINK_TYPE,
            validators.getFirst().getExample()
        ));
    }

    @Test
    public void testTrackFail_SUCCESSFUL_TRACK_MESSAGE() {
        // Setting mockUpdate
        var mockUpdate = Mockito.mock(Update.class);
        var mockedMessage = Mockito.mock(Message.class);
        Mockito.when(mockUpdate.getMessage()).thenReturn(mockedMessage);
        Mockito.when(mockUpdate.getMessage().getText()).thenReturn("/track https://github.com/lztd1/tinkoff_edu");

        // Setting mock scrapperClient
        var mockScrapperClient = Mockito.mock(ScrapperClient.class);

        // Creating args
        var validators = List.of(
            new GithubValidator(),
            new StackoverflowValidator()
        );

        // Assertions
        var method = new TrackHandler(mockScrapperClient, validators);
        assertThat(method.handle(mockUpdate)).isEqualTo(SUCCESSFUL_TRACK_MESSAGE);
    }

    private String getSupportedResources(List<LinkValidator> validators) {
        return String.join(
            "\n",
            validators.stream()
                .map(LinkValidator::getHost)
                .toList()
        );
    }
}
