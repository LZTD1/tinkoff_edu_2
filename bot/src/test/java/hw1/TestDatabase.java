package hw1;

import edu.java.database.Database;
import edu.java.database.SimpleDatabase;
import org.junit.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestDatabase {

    private Database database = new SimpleDatabase();

    @Test
    public void testRegisterUserDatabase() {
        Long userId = 1L;

        assertThat(database.isUserRegister(userId)).isFalse();

        database.registerUser(userId);

        assertThat(database.isUserRegister(userId)).isTrue();
    }

    @Test
    public void testAddLink() {
        Long userId = 2L;

        assertThat(database.getUserLinksById(userId).size()).isEqualTo(0);

        database.addLink(userId, "test");

        assertThat(database.getUserLinksById(userId).size()).isEqualTo(1);
    }

    @Test
    public void testRemoveLink() {
        Long userId = 3L;

        database.addLink(userId, "test");
        assertThat(database.getUserLinksById(userId).size()).isEqualTo(1);

        database.removeLink(userId, "test");
        assertThat(database.getUserLinksById(userId).size()).isEqualTo(0);
    }
}
