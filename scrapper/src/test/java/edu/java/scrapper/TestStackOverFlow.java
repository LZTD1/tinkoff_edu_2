package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.java.clients.StackoverflowClient;
import edu.java.dto.sofDto.ItemDto;
import edu.java.dto.sofDto.OwnerDto;
import edu.java.dto.sofDto.SofResponseDto;
import java.util.ArrayList;
import org.junit.Rule;
import org.junit.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

public class TestStackOverFlow {

    public static final SofResponseDto IDEAL_RESPONSE = new SofResponseDto() {{
        quotaMax = 300;
        quotaRemaining = 277;
        hasMore = false;
        items = new ArrayList<>() {{
            add(new ItemDto() {{
                isAccepted = true;
                score = 1;
                lastActivityDate = 1586185945;
                creationDate = 1586185945;
                answerId = 61062973;
                questionId = 61033836;
                contentLicense = "CC BY-SA 4.0";
                owner = new OwnerDto() {{
                    accountId = 7498967;
                    reputation = 200;
                    userId = 5698120;
                    userType = "registered";
                    profileImage =
                        "https://www.gravatar.com/avatar/3ba3535d92e44eb78b30ef574102b398?s=256&d=identicon&r=PG";
                    displayName = "Rex Raphael";
                    link = "https://stackoverflow.com/users/5698120/rex-raphael";
                }};
            }});
        }};
    }};
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(80));

    @Test
    public void sofTest() {
        configStub();

        SofResponseDto response = new StackoverflowClient("localhost")
            .getAnswersByQuestion(1)
            .block();

        assertThat(IDEAL_RESPONSE.quotaMax).isEqualTo(response.quotaMax); // через assertEquals() сравнивает все равно ссылки
        assertThat(IDEAL_RESPONSE.quotaRemaining).isEqualTo(response.quotaRemaining); // поэтому решил так костыльно
        assertThat(IDEAL_RESPONSE.items.get(0).answerId).isEqualTo(response.items.get(0).answerId);
        assertThat(IDEAL_RESPONSE.items.get(0).questionId).isEqualTo(response.items.get(0).questionId);
        assertThat(IDEAL_RESPONSE.items.get(0).owner.accountId).isEqualTo(response.items.get(0).owner.accountId);
    }

    private void configStub() {
        var jsonString =
            "{\"items\":[{\"owner\":{\"account_id\":7498967,\"reputation\":200,\"user_id\":5698120,\"user_type\":\"registered\",\"profile_image\":\"https://www.gravatar.com/avatar/3ba3535d92e44eb78b30ef574102b398?s=256&d=identicon&r=PG\",\"display_name\":\"Rex Raphael\",\"link\":\"https://stackoverflow.com/users/5698120/rex-raphael\"},\"is_accepted\":true,\"score\":1,\"last_activity_date\":1586185945,\"creation_date\":1586185945,\"answer_id\":61062973,\"question_id\":61033836,\"content_license\":\"CC BY-SA 4.0\"}],\"has_more\":false,\"quota_max\":300,\"quota_remaining\":277}";

        configureFor("localhost", 80);
        stubFor(get(urlEqualTo("/2.3/questions/1/answers?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
                    .withBody(jsonString)
            )
        );
    }

}
