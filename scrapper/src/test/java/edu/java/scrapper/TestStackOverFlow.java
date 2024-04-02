package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.StackoverflowClient;
import edu.java.clients.dto.sofDto.ItemsDto;
import edu.java.clients.dto.sofDto.OwnerSofDto;
import edu.java.clients.dto.sofDto.StackOverFlowDto;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

@WireMockTest
public class TestStackOverFlow {

    public static final StackOverFlowDto IDEAL_ANSWERS_DTO = new StackOverFlowDto() {{
        setItems(new ArrayList<>() {{
            add(new ItemsDto() {{
                setOwner(new OwnerSofDto() {{
                    setDisplayName("jin");
                    setLink(URI.create("https://stackoverflow.com/users/1371719/jin"));
                    setReputation(1542L);
                }});
                setBody("helloWorld");
                setCreationDate(Instant.ofEpochSecond(1381406581).atOffset(ZoneOffset.UTC));
            }});
        }});
    }};
    public static final StackOverFlowDto IDEAL_COMMENTS_DTO = new StackOverFlowDto() {{
        setItems(new ArrayList<>() {{
            add(new ItemsDto() {{
                setOwner(new OwnerSofDto() {{
                    setDisplayName("Abhinav Rana");
                    setLink(URI.create("https://stackoverflow.com/users/5846082/abhinav-rana"));
                    setReputation(63L);
                }});
                setBody("Thanks for the inputs guys !!!");
                setCreationDate(Instant.ofEpochSecond(1526197871).atOffset(ZoneOffset.UTC));
            }});
        }});
    }};
    private static RetryTemplate mockRetryTemplate;
    private static RetryContext mockContext;

    WireMockServer wireMockServer;

    @BeforeAll
    static void beforeAll(){
        mockRetryTemplate = Mockito.mock(RetryTemplate.class);
        mockContext = Mockito.mock(RetryContext.class);

        Mockito.when(mockContext.getRetryCount()).thenReturn(1);
        Mockito.when(mockRetryTemplate.execute(Mockito.any())).thenAnswer(invocation -> {
            RetryCallback callback = invocation.getArgument(0);
            return callback.doWithRetry(mockContext);
        });
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
    public void sofAnswersTest() {


        StackOverFlowDto response = new StackoverflowClient("http://localhost:3000", mockRetryTemplate)
            .getAnswersByQuestion(1);

        assertThat(response.getItems().size()).isEqualTo(1);
        assertThat(response.getItems().getFirst().getBody()).isEqualTo(IDEAL_ANSWERS_DTO.getItems().getFirst()
            .getBody());
        assertThat(response.getItems().getFirst().getOwner().getDisplayName()).isEqualTo(IDEAL_ANSWERS_DTO.getItems()
            .getFirst().getOwner().getDisplayName());
        assertThat(response.getItems().getFirst().getCreationDate()).isEqualTo(IDEAL_ANSWERS_DTO.getItems().getFirst()
            .getCreationDate());
    }

    @Test
    public void sofCommentsTest() {
        StackOverFlowDto response = new StackoverflowClient("http://localhost:3000", mockRetryTemplate)
            .getCommentsByQuestion(1);

        assertThat(response.getItems().size()).isEqualTo(1);
        assertThat(response.getItems().getFirst().getBody()).isEqualTo(IDEAL_COMMENTS_DTO.getItems().getFirst()
            .getBody());
        assertThat(response.getItems().getFirst().getOwner().getDisplayName()).isEqualTo(IDEAL_COMMENTS_DTO.getItems()
            .getFirst().getOwner().getDisplayName());
        assertThat(response.getItems().getFirst().getCreationDate()).isEqualTo(IDEAL_COMMENTS_DTO.getItems().getFirst()
            .getCreationDate());
    }

    private void configStub() {
        var jsonString =
            "{\"items\":[{\"owner\":{\"account_id\":1456206,\"reputation\":1542,\"user_id\":1371719,\"user_type\":\"registered\",\"profile_image\":\"https://www.gravatar.com/avatar/3f3e14183abeee688a5d9be3bc220b97?s=256&d=identicon&r=PG\",\"display_name\":\"jin\",\"link\":\"https://stackoverflow.com/users/1371719/jin\"},\"is_accepted\":true,\"score\":2,\"last_activity_date\":1381406581,\"creation_date\":1381406581,\"answer_id\":19295256,\"question_id\":19294916,\"content_license\":\"CC BY-SA 3.0\",\"body\":\"helloWorld\"}],\"has_more\":false,\"quota_max\":300,\"quota_remaining\":288}";

        configureFor("localhost", 3000);
        stubFor(get(urlEqualTo("/2.3/questions/1/answers?order=desc&sort=creation&site=stackoverflow&filter=withbody"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
                    .withBody(jsonString)
            )
        );

        var jsonString2 =
            "{\"items\":[{\"owner\":{\"account_id\":7719407,\"reputation\":63,\"user_id\":5846082,\"user_type\":\"registered\",\"profile_image\":\"https://graph.facebook.com/10203926139754129/picture?type=large\",\"display_name\":\"Abhinav Rana\",\"link\":\"https://stackoverflow.com/users/5846082/abhinav-rana\"},\"edited\":false,\"score\":0,\"creation_date\":1526197871,\"post_id\":50303045,\"comment_id\":87643806,\"content_license\":\"CC BY-SA 4.0\",\"body\":\"Thanks for the inputs guys !!!\"}],\"has_more\":false,\"quota_max\":300,\"quota_remaining\":287}";

        configureFor("localhost", 3000);
        stubFor(get(urlEqualTo("/2.3/questions/1/comments?order=desc&sort=creation&site=stackoverflow&filter=withbody"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
                    .withBody(jsonString2)
            )
        );

    }
}
