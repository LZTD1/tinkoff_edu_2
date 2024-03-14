package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.GithubClient;
import edu.java.clients.dto.githubDto.CommitDto;
import edu.java.clients.dto.githubDto.CommitterDto;
import edu.java.clients.dto.githubDto.GitResponseDto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

@WireMockTest
public class TestGithub {

    public static final GitResponseDto IDEAL_RESPONSE = new GitResponseDto() {{
        sha = "6b99dea062f28e723b2022e0aa0e3d17e05f6566";
        nodeId = "C_kwDOJcbWa9oAKDZiOTlkZWEwNjJmMjhlNzIzYjIwMjJlMGFhMGUzZDE3ZTA1ZjY1NjY";
        commit = new CommitDto() {{
            message = "commmiiiiit!";
            committer = new CommitterDto() {{
                name = "LZTD1";
                email = "danil227pavlov@gmail.com";
            }};
        }};
    }};
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
    public void gitTest() {
        List<GitResponseDto> response = new GithubClient("http://localhost:3000")
            .getAnswersByQuestion("LZTD1", "Voronezh-Hakaton")
            .collectList()
            .block();

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).nodeId).isEqualTo(IDEAL_RESPONSE.nodeId);
        assertThat(response.get(0).sha).isEqualTo(IDEAL_RESPONSE.sha);
        assertThat(response.get(0).commit.message).isEqualTo(IDEAL_RESPONSE.commit.message);
    }

    private void configStub() {
        var jsonString =
            "[ { \"sha\": \"6b99dea062f28e723b2022e0aa0e3d17e05f6566\", \"node_id\": \"C_kwDOJcbWa9oAKDZiOTlkZWEwNjJmMjhlNzIzYjIwMjJlMGFhMGUzZDE3ZTA1ZjY1NjY\", \"commit\": { \"author\": { \"name\": \"LZTD1\", \"email\": \"danil227pavlov@gmail.com\", \"date\": \"2023-04-28T09:28:25Z\" }, \"committer\": { \"name\": \"LZTD1\", \"email\": \"danil227pavlov@gmail.com\", \"date\": \"2023-04-28T09:28:25Z\" }, \"message\": \"commmiiiiit!\", \"tree\": { \"sha\": \"3406bed9a73f0ed880647ed679dd59beb2094325\", \"url\": \"https://api.github.com/repos/LZTD1/Voronezh-Hakaton/git/trees/3406bed9a73f0ed880647ed679dd59beb2094325\" }, \"url\": \"https://api.github.com/repos/LZTD1/Voronezh-Hakaton/git/commits/6b99dea062f28e723b2022e0aa0e3d17e05f6566\", \"comment_count\": 0, \"verification\": { \"verified\": false, \"reason\": \"unsigned\", \"signature\": null, \"payload\": null } }, \"url\": \"https://api.github.com/repos/LZTD1/Voronezh-Hakaton/commits/6b99dea062f28e723b2022e0aa0e3d17e05f6566\", \"html_url\": \"https://github.com/LZTD1/Voronezh-Hakaton/commit/6b99dea062f28e723b2022e0aa0e3d17e05f6566\", \"comments_url\": \"https://api.github.com/repos/LZTD1/Voronezh-Hakaton/commits/6b99dea062f28e723b2022e0aa0e3d17e05f6566/comments\", \"author\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"committer\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"parents\": [ ] } ]";

        configureFor("localhost", 3000);
        stubFor(get(urlEqualTo("/repos/LZTD1/Voronezh-Hakaton/commits"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
                    .withBody(jsonString)
            )
        );
    }

}
