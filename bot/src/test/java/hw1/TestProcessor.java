package hw1;

import database.Database;
import database.SimpleDatabase;
import org.junit.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static processor.Constants.*;
import static processor.Processor.getAnswer;

public class TestProcessor {

    private final Database database = new SimpleDatabase();

    @Test
    public void testStart(){
        String ans = getAnswer("/start", 1L, database);

        assertThat(ans).isEqualTo(START_MESSAGE);
    }
    @Test
    public void testUnknownCommand(){
        String ans = getAnswer("-1", 1L, database);

        assertThat(ans).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    public void testEmptyListMessage(){
        String ans = getAnswer("/list", 1L, database);

        assertThat(ans).isEqualTo(EMPTY_LIST_MESSAGE);
    }

    @Test
    public void testSucessfullTrackMessage(){
        String ans = getAnswer("/track vk.com", 1L, database);

        assertThat(ans).isEqualTo(SUCCESSFUL_TRACK_MESSAGE);
    }
    @Test
    public void testSucessfullUnTrackMessage(){
        String ans = getAnswer("/untrack vk.com", 1L, database);

        assertThat(ans).isEqualTo(SUCCESSFUL_UNTRACK_MESSAGE);
    }

    @Test
    public void testFailedTrackMessage_NoParams(){
        String ans = getAnswer("/track", 1L, database);

        assertThat(ans).isEqualTo(FAIL_TRACK_MESSAGE);
    }
    @Test
    public void testFailedTrackMessage_More2Param(){
        String ans = getAnswer("/track 1 2 3", 1L, database);

        assertThat(ans).isEqualTo(FAIL_TRACK_MESSAGE);
    }
    @Test
    public void testFailedUnTrackMessage_NoParams(){
        String ans = getAnswer("/untrack", 1L, database);

        assertThat(ans).isEqualTo(FAIL_UNTRACK_MESSAGE);
    }
    @Test
    public void testFailedUnTrackMessage_More2Param(){
        String ans = getAnswer("/untrack 1 2 3", 1L, database);

        assertThat(ans).isEqualTo(FAIL_UNTRACK_MESSAGE);
    }
    @Test
    public void testHelpMessage(){
        String ans = getAnswer("/help", 1L, database);

        assertThat(ans).isEqualTo(HELP_MESSAGE);
    }
}
