package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.BotClient;
import edu.java.clients.exceptions.BadQueryParamsException;
import java.net.URI;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

@WireMockTest
public class TestBot {

    WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(3000);
        wireMockServer.start();
        configStub();
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    @Test
    public void botTest2xx() {
        new BotClient("http://localhost:3000")
            .sendUpdate(1L, URI.create("vk.com"), "desc", new ArrayList<>());
    }

    @Test
    public void botTest4xx() {
        assertThrows(
            BadQueryParamsException.class,
            () -> new BotClient("http://localhost:3000")
                .sendUpdate(2L, URI.create("vk.com"), "desc", new ArrayList<>())
        );

    }

    private void configStub() {
        configureFor("localhost", 3000);

        stubFor(post(urlPathEqualTo("/updates"))
            .withRequestBody(equalToJson(
                "{ \"id\": 1, \"url\": \"vk.com\", \"description\": \"desc\", \"tgChatIds\": [] }"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
            )
        );
        stubFor(post(urlPathEqualTo("/updates"))
            .withRequestBody(equalToJson(
                "{ \"id\": 2, \"url\": \"vk.com\", \"description\": \"desc\", \"tgChatIds\": [] }"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(400)
            )
        );
    }

}
