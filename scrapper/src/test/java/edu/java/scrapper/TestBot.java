package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.java.clients.BotClient;
import java.net.URI;
import java.util.ArrayList;
import org.junit.Rule;
import org.junit.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

public class TestBot {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(3000));

    @Test
    public void botTest() {
        configStub();

        int response = new BotClient("http://localhost:3000")
            .getAnswersByQuestion(1L, URI.create("vk.com"), "desc", new ArrayList<>())
            .block()
            .getStatusCode()
            .value();

        assertThat(response).isEqualTo(200);
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
    }

}
