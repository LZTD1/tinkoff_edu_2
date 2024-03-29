package edu.java.scrapper.domain.jpa;

import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.jpa.JpaUserLinkRelRepository;
import edu.java.domain.jpa.JpaUserRepository;
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
    private JpaUserLinkRelRepository jpaUserLinkRelRepository;
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void testFindAllByUserid_TelegramId() {
        var user = new User();
        user.setTelegramId(123L);
        jpaUserRepository.saveAndFlush(user);

        var link = new Link();
        link.setLink(URI.create("https://vk.com"));
        jpaLinkRepository.saveAndFlush(link);

        var userLinkRel = new UserLinkRel();
        userLinkRel.setUser(user);
        userLinkRel.setLink(link);
        jpaUserLinkRelRepository.saveAndFlush(userLinkRel);

        List<UserLinkRel> res = jpaUserLinkRelRepository.findByUserTelegramId(123L);

        assertThat(res.getFirst().getLink().getLink()).isEqualTo(URI.create("https://vk.com"));
    }

    @Test
    @Transactional
    @Rollback
    void testFindByLinkid_Id() {
        var user = new User();
        user.setTelegramId(123L);
        jpaUserRepository.saveAndFlush(user);

        var link = new Link();
        link.setLink(URI.create("https://vk.com"));
        jpaLinkRepository.saveAndFlush(link);

        var userLinkRel = new UserLinkRel();
        userLinkRel.setUser(user);
        userLinkRel.setLink(link);
        jpaUserLinkRelRepository.saveAndFlush(userLinkRel);

        List<UserLinkRel> res = jpaUserLinkRelRepository.findByLinkId(link.getId());

        assertThat(res.getFirst().getUser().getTelegramId()).isEqualTo(123L);
    }
}
