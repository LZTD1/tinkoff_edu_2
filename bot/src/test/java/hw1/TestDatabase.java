package hw1;

import edu.java.database.SimpleDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestDatabase {

    public final SimpleDatabase db = SimpleDatabase.getInstance();

    @AfterEach
    public void dropBase() {
        db.dropAll();
    }

    @Test
    public void testRegisterUserDatabase() {
        Long userId = 1L;

        assertThat(db.isUserRegister(userId)).isFalse();

        db.registerUser(userId);

        assertThat(db.isUserRegister(userId)).isTrue();
    }

    @Test
    public void testAddLink() {
        Long userId = 1L;

        assertThat(db.getUserLinksById(userId).size()).isEqualTo(0);

        db.addLink(userId, "test");

        assertThat(db.getUserLinksById(userId).size()).isEqualTo(1);
    }

    @Test
    public void testRemoveLink() {
        Long userId = 1L;

        db.addLink(userId, "test");
        assertThat(db.getUserLinksById(userId).size()).isEqualTo(1);

        db.removeLink(userId, "test");
        assertThat(db.getUserLinksById(userId).size()).isEqualTo(0);
    }
}
