package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.java.GithubClient;
import edu.java.entity.git.GitResponse;
import edu.java.entity.git.User;
import java.time.OffsetDateTime;
import java.util.List;
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

public class TestGithub {

    public static final GitResponse IDEAL_RESPONSE = new GitResponse() {{
        url = "https://api.github.com/repos/massgravel/Microsoft-Activation-Scripts/issues/comments/1951875729";
        issueUrl = "https://api.github.com/repos/massgravel/Microsoft-Activation-Scripts/issues/369";
        id = 1951875729;
        nodeId = "IC_kwDODeqAx850V0KR";
        user = new User() {{
            login = "WindowsAddict";
            id = 40813939;
            nodeId = "MDQ6VXNlcjQwODEzOTM5";
            avatarUrl = "https://avatars.githubusercontent.com/u/40813939?v=4";
            url = "https://api.github.com/users/WindowsAddict";
        }};
        createdAt = OffsetDateTime.parse("2024-02-19T07:47:51Z");
        updatedAt = OffsetDateTime.parse("2024-02-19T07:47:51Z");
    }};
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(80));

    @Test
    public void sofTest() {
        configStub();

        List<GitResponse> response = new GithubClient()
            .getAnswersByQuestion("massgravel", "Microsoft-Activation-Scripts", 369)
            .collectList()
            .block();

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).user.nodeId).isEqualTo(IDEAL_RESPONSE.user.nodeId);
        assertThat(response.get(0).user.nodeId).isEqualTo(IDEAL_RESPONSE.user.nodeId);
        assertThat(response.get(0).nodeId).isEqualTo(IDEAL_RESPONSE.nodeId);
    }

    private void configStub() {
        var jsonString =
            "[ { \"url\": \"https://api.github.com/repos/massgravel/Microsoft-Activation-Scripts/issues/comments/1951875729\", \"html_url\": \"https://github.com/massgravel/Microsoft-Activation-Scripts/issues/369#issuecomment-1951875729\", \"issue_url\": \"https://api.github.com/repos/massgravel/Microsoft-Activation-Scripts/issues/369\", \"id\": 1951875729, \"node_id\": \"IC_kwDODeqAx850V0KR\", \"user\": { \"login\": \"WindowsAddict\", \"id\": 40813939, \"node_id\": \"MDQ6VXNlcjQwODEzOTM5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/40813939?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/WindowsAddict\", \"html_url\": \"https://github.com/WindowsAddict\", \"followers_url\": \"https://api.github.com/users/WindowsAddict/followers\", \"following_url\": \"https://api.github.com/users/WindowsAddict/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/WindowsAddict/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/WindowsAddict/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/WindowsAddict/subscriptions\", \"organizations_url\": \"https://api.github.com/users/WindowsAddict/orgs\", \"repos_url\": \"https://api.github.com/users/WindowsAddict/repos\", \"events_url\": \"https://api.github.com/users/WindowsAddict/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/WindowsAddict/received_events\", \"type\": \"User\", \"site_admin\": false }, \"created_at\": \"2024-02-19T07:47:51Z\", \"updated_at\": \"2024-02-19T07:47:51Z\", \"author_association\": \"MEMBER\", \"body\": \"Run HWID option, share screenshot. \", \"reactions\": { \"url\": \"https://api.github.com/repos/massgravel/Microsoft-Activation-Scripts/issues/comments/1951875729/reactions\", \"total_count\": 0, \"+1\": 0, \"-1\": 0, \"laugh\": 0, \"hooray\": 0, \"confused\": 0, \"heart\": 0, \"rocket\": 0, \"eyes\": 0 }, \"performed_via_github_app\": null } ]";

        configureFor("localhost", 80);
        stubFor(get(urlEqualTo("/repos/massgravel/Microsoft-Activation-Scripts/issues/369/comments"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
                    .withBody(jsonString)
            )
        );
    }

}
