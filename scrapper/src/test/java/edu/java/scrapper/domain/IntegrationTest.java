package edu.java.scrapper.domain;

import java.io.File;
import java.nio.file.Path;
import java.sql.DriverManager;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.SneakyThrows;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class IntegrationTest {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    @SneakyThrows
    private static void runMigrations(JdbcDatabaseContainer<?> c) {
        Path locationMaster =
            new File(".")
                .toPath()
                .toAbsolutePath()
                .getParent()
                .getParent()
                .resolve("migrations")
                .resolve("liquibase")
                .resolve("changelog");

        try (
            var liqBase = new Liquibase(
                "master.yaml",
                new DirectoryResourceAccessor(locationMaster),
                new JdbcConnection(
                    DriverManager.getConnection(
                        c.getJdbcUrl(),
                        c.getUsername(),
                        c.getPassword()
                    )
                )
            );
        ) {
            liqBase.update();
        }

    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
