package edu.java.scrapper.domain.jpa;

import edu.java.domain.jpa.LinkRepository;
import edu.java.domain.jpa.UserLinkRelRepository;
import edu.java.domain.jpa.UserRepository;
import edu.java.dto.Link;
import edu.java.dto.User;
import edu.java.dto.UserLinkRel;
import edu.java.scrapper.domain.IntegrationTest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestRelationalTransactions extends IntegrationTest {

    @Autowired
    private UserLinkRelRepository userLinkRelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testFindAllByUserid_TelegramId() {
        var user = new User();
        user.setTelegramId(123L);
        userRepository.save(user);

        var link = new Link();
        link.setLink(URI.create("https://vk.com"));
        linkRepository.save(link);

        var userLinkRel = new UserLinkRel();
        userLinkRel.setUser(user);
        userLinkRel.setLink(link);
        userLinkRelRepository.save(userLinkRel);

        List<UserLinkRel> res = userLinkRelRepository.findByUserTelegramId(123L);

        assertThat(res.getFirst().getLink().getLink()).isEqualTo(URI.create("https://vk.com"));
    }

    @Test
    @Transactional
    @Rollback
    void testFindByLinkid_Id() {
        var user = new User();
        user.setTelegramId(123L);
        userRepository.save(user);

        var link = new Link();
        link.setLink(URI.create("https://vk.com"));
        linkRepository.save(link);

        var userLinkRel = new UserLinkRel();
        userLinkRel.setUser(user);
        userLinkRel.setLink(link);
        userLinkRelRepository.save(userLinkRel);

        List<UserLinkRel> res = userLinkRelRepository.findByLinkId(link.getId());

        assertThat(res.getFirst().getUser().getTelegramId()).isEqualTo(123L);
    }
}
