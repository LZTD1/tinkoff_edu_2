package hw1.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.clients.exceptions.LinkNotFoundException;
import edu.java.scrapper.dto.ListLinksResponse;
import java.net.URI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WireMockTest
public class TestControllerLinks {

    WireMockServer wireMockServer;

    private static void configStub() {
        configureFor("localhost", 3000);

        stubFor(get(urlPathEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
                    .withBody("{ \"links\": [ { \"id\": 0, \"url\": \"https://vk.com\" } ], \"size\": 1 }")
            )
        );
        stubFor(get(urlPathEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("0"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
                    .withBody("{ \"links\": [ { \"id\": 0, \"url\": \"https://vk.com\" } ], \"size\": 1 }")
            )
        );
        // addLink
        stubFor(post(urlPathEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .withRequestBody(equalToJson(
                "{ \"link\": \"https://vk.com\" }"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
            )
        );

        stubFor(delete(urlPathEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .withRequestBody(equalToJson(
                "{ \"link\": \"https://vk.com\" }"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
            )
        );
        stubFor(delete(urlPathEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("0"))
            .withRequestBody(equalToJson(
                "{ \"link\": \"https://vk.com\" }"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(404)
            )
        );
    }

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
    public void getAllLinks() {
        ListLinksResponse response = new ScrapperClient("http://localhost:3000")
            .getAllTrackedLinks(1L, 10, 0);

        assertThat(response.getLinks().get(0).getUrl()).isEqualTo(URI.create("https://vk.com"));
    }

    @Test
    public void deleteLink2xx() {
        new ScrapperClient("http://localhost:3000")
            .deleteTrackLink(1L, URI.create("https://vk.com"));
    }

    @Test
    public void deleteLink404() {
        assertThrows(LinkNotFoundException.class, () -> new ScrapperClient("http://localhost:3000")
            .deleteTrackLink(10L, URI.create("https://vk.com")));
    }

    @Test
    public void deleteLink400() {
        assertThrows(LinkNotFoundException.class, () -> new ScrapperClient("http://localhost:3000")
            .deleteTrackLink(0L, URI.create("https://vk.com")));
    }

    @Test
    public void addLink() {
        new ScrapperClient("http://localhost:3000")
            .addTrackLink(1L, URI.create("https://vk.com"));
    }
}
