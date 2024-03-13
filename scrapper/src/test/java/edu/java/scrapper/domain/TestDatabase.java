package edu.java.scrapper.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDatabase extends IntegrationTest {

    private static JdbcTemplate jdbc;

    @BeforeAll
    static void setUp() {
        jdbc = new JdbcTemplate(
            DataSourceBuilder
                .create()
                .url(POSTGRES.getJdbcUrl())
                .username(POSTGRES.getUsername())
                .password(POSTGRES.getPassword())
                .build()
        );
    }

    @Test
    void testPostgresIsRunning() {
        assertTrue(POSTGRES.isRunning());
    }

    @Test
    void testCreateUserEntity() {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer tableExists = jdbc.queryForObject(sql, Integer.class, "users");
        assertThat(tableExists.intValue()).isEqualTo(1);
    }

    @Test
    void testCreateLinkEntity() {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer tableExists = jdbc.queryForObject(sql, Integer.class, "links");
        assertThat(tableExists.intValue()).isEqualTo(1);
    }

    @Test
    void testCreateUserLinkTable() {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer tableExists = jdbc.queryForObject(sql, Integer.class, "users_links");
        assertThat(tableExists.intValue()).isEqualTo(1);
    }
}
