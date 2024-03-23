package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.GithubClient;
import edu.java.clients.dto.githubDto.UserDto;
import edu.java.clients.dto.githubDto.commit.CommitsDto;
import edu.java.clients.dto.githubDto.commit.commit.CommitDto;
import edu.java.clients.dto.githubDto.commit.commit.EntityDto;
import edu.java.clients.dto.githubDto.pull.PullDto;
import java.net.URI;
import java.time.OffsetDateTime;
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

    public static final PullDto IDEAL_RESPONSE_PULL = new PullDto() {{
        setBody("helloWorld");
        setId(1772362669L);
        setHtmlUrl(URI.create("https://github.com/LZTD1/tinkoff_edu_2/pull/5"));
        setCreatedAt(OffsetDateTime.parse("2024-03-14T14:38:47Z"));
    }};
    public static final CommitsDto IDEAL_RESPONSE_COMMIT = new CommitsDto() {{
        setHtmlUrl(URI.create(
            "https://github.com/LZTD1/Voronezh-Hakaton/commit/6b99dea062f28e723b2022e0aa0e3d17e05f6566"));
        setNodeId("C_kwDOJcbWa9oAKDZiOTlkZWEwNjJmMjhlNzIzYjIwMjJlMGFhMGUzZDE3ZTA1ZjY1NjY");
        setAuthor(new UserDto() {{
            setLogin("LZTD1");
            setId(46750499L);
            setHtmlUrl(URI.create("https://github.com/LZTD1"));
        }});
        setCommit(
            new CommitDto() {{
                setMessage("commmiiiiit!");
                setAuthor(new EntityDto() {{
                    setName("LZTD1");
                    setEmail("danil227pavlov@gmail.com");
                    setDate(OffsetDateTime.parse("2023-04-28T09:28:25Z"));
                }});
                setCommitter(new EntityDto() {{
                    setName("LZTD1");
                    setEmail("danil227pavlov@gmail.com");
                    setDate(OffsetDateTime.parse("2023-04-28T09:28:25Z"));
                }});
            }}
        );
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
    public void gitPullsTest() {
        List<PullDto> response = new GithubClient("http://localhost:3000")
            .getPullsByRepos("LZTD1", "tinkoff_edu_2")
            .collectList()
            .block();

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.getFirst().getId()).isEqualTo(IDEAL_RESPONSE_PULL.getId());
        assertThat(response.getFirst().getHtmlUrl()).isEqualTo(IDEAL_RESPONSE_PULL.getHtmlUrl());
        assertThat(response.getFirst().getBody()).isEqualTo(IDEAL_RESPONSE_PULL.getBody());
        assertThat(response.getFirst().getCreatedAt()).isEqualTo(IDEAL_RESPONSE_PULL.getCreatedAt());
    }

    @Test
    public void gitCommitsTest() {
        List<CommitsDto> response = new GithubClient("http://localhost:3000")
            .getCommitsByRepos("LZTD1", "Voronezh-Hakaton")
            .collectList()
            .block();

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.getFirst().getNodeId()).isEqualTo(IDEAL_RESPONSE_COMMIT.getNodeId());
        assertThat(response.getFirst().getHtmlUrl()).isEqualTo(IDEAL_RESPONSE_COMMIT.getHtmlUrl());
        assertThat(response.getFirst().getAuthor().getId()).isEqualTo(IDEAL_RESPONSE_COMMIT.getAuthor().getId());
        assertThat(response.getFirst().getCommit().getCommitter()
            .getEmail()).isEqualTo(IDEAL_RESPONSE_COMMIT.getCommit().getCommitter().getEmail());
    }

    private void configStub() {
        var jsonString =
            "[ { \"url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/pulls/5\", \"id\": 1772362669, \"node_id\": \"PR_kwDOLPh57s5ppBut\", \"html_url\": \"https://github.com/LZTD1/tinkoff_edu_2/pull/5\", \"diff_url\": \"https://github.com/LZTD1/tinkoff_edu_2/pull/5.diff\", \"patch_url\": \"https://github.com/LZTD1/tinkoff_edu_2/pull/5.patch\", \"issue_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/issues/5\", \"number\": 5, \"state\": \"open\", \"locked\": false, \"title\": \"Hw5\", \"user\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"body\": \"helloWorld\", \"created_at\": \"2024-03-14T14:38:47Z\", \"updated_at\": \"2024-03-18T08:22:54Z\", \"closed_at\": null, \"merged_at\": null, \"merge_commit_sha\": \"95dfa7cdca051732dc42167cee793c1cedeeb3d1\", \"assignee\": null, \"assignees\": [ ], \"requested_reviewers\": [ ], \"requested_teams\": [ ], \"labels\": [ ], \"milestone\": null, \"draft\": false, \"commits_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/pulls/5/commits\", \"review_comments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/pulls/5/comments\", \"review_comment_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/pulls/comments{/number}\", \"comments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/issues/5/comments\", \"statuses_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/statuses/e9e0e98c7562c2ec47d6ec23eb2762ddb295cdf3\", \"head\": { \"label\": \"LZTD1:hw5\", \"ref\": \"hw5\", \"sha\": \"e9e0e98c7562c2ec47d6ec23eb2762ddb295cdf3\", \"user\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"repo\": { \"id\": 754481646, \"node_id\": \"R_kgDOLPh57g\", \"name\": \"tinkoff_edu_2\", \"full_name\": \"LZTD1/tinkoff_edu_2\", \"private\": false, \"owner\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"html_url\": \"https://github.com/LZTD1/tinkoff_edu_2\", \"description\": null, \"fork\": false, \"url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2\", \"forks_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/forks\", \"keys_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/keys{/key_id}\", \"collaborators_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/collaborators{/collaborator}\", \"teams_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/teams\", \"hooks_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/hooks\", \"issue_events_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/issues/events{/number}\", \"events_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/events\", \"assignees_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/assignees{/user}\", \"branches_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/branches{/branch}\", \"tags_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/tags\", \"blobs_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/git/blobs{/sha}\", \"git_tags_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/git/tags{/sha}\", \"git_refs_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/git/refs{/sha}\", \"trees_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/git/trees{/sha}\", \"statuses_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/statuses/{sha}\", \"languages_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/languages\", \"stargazers_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/stargazers\", \"contributors_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/contributors\", \"subscribers_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/subscribers\", \"subscription_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/subscription\", \"commits_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/commits{/sha}\", \"git_commits_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/git/commits{/sha}\", \"comments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/comments{/number}\", \"issue_comment_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/issues/comments{/number}\", \"contents_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/contents/{+path}\", \"compare_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/compare/{base}...{head}\", \"merges_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/merges\", \"archive_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/{archive_format}{/ref}\", \"downloads_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/downloads\", \"issues_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/issues{/number}\", \"pulls_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/pulls{/number}\", \"milestones_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/milestones{/number}\", \"notifications_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/notifications{?since,all,participating}\", \"labels_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/labels{/name}\", \"releases_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/releases{/id}\", \"deployments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/deployments\", \"created_at\": \"2024-02-08T06:22:28Z\", \"updated_at\": \"2024-02-08T06:23:38Z\", \"pushed_at\": \"2024-03-18T08:21:36Z\", \"git_url\": \"git://github.com/LZTD1/tinkoff_edu_2.git\", \"ssh_url\": \"git@github.com:LZTD1/tinkoff_edu_2.git\", \"clone_url\": \"https://github.com/LZTD1/tinkoff_edu_2.git\", \"svn_url\": \"https://github.com/LZTD1/tinkoff_edu_2\", \"homepage\": null, \"size\": 206, \"stargazers_count\": 0, \"watchers_count\": 0, \"language\": \"Java\", \"has_issues\": true, \"has_projects\": true, \"has_downloads\": true, \"has_wiki\": true, \"has_pages\": false, \"has_discussions\": false, \"forks_count\": 0, \"mirror_url\": null, \"archived\": false, \"disabled\": false, \"open_issues_count\": 1, \"license\": null, \"allow_forking\": true, \"is_template\": false, \"web_commit_signoff_required\": false, \"topics\": [ ], \"visibility\": \"public\", \"forks\": 0, \"open_issues\": 1, \"watchers\": 0, \"default_branch\": \"master\" } }, \"base\": { \"label\": \"LZTD1:master\", \"ref\": \"master\", \"sha\": \"fac374bace886c21258b85ab023bbcb77a093344\", \"user\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"repo\": { \"id\": 754481646, \"node_id\": \"R_kgDOLPh57g\", \"name\": \"tinkoff_edu_2\", \"full_name\": \"LZTD1/tinkoff_edu_2\", \"private\": false, \"owner\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"html_url\": \"https://github.com/LZTD1/tinkoff_edu_2\", \"description\": null, \"fork\": false, \"url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2\", \"forks_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/forks\", \"keys_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/keys{/key_id}\", \"collaborators_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/collaborators{/collaborator}\", \"teams_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/teams\", \"hooks_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/hooks\", \"issue_events_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/issues/events{/number}\", \"events_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/events\", \"assignees_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/assignees{/user}\", \"branches_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/branches{/branch}\", \"tags_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/tags\", \"blobs_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/git/blobs{/sha}\", \"git_tags_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/git/tags{/sha}\", \"git_refs_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/git/refs{/sha}\", \"trees_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/git/trees{/sha}\", \"statuses_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/statuses/{sha}\", \"languages_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/languages\", \"stargazers_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/stargazers\", \"contributors_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/contributors\", \"subscribers_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/subscribers\", \"subscription_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/subscription\", \"commits_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/commits{/sha}\", \"git_commits_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/git/commits{/sha}\", \"comments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/comments{/number}\", \"issue_comment_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/issues/comments{/number}\", \"contents_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/contents/{+path}\", \"compare_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/compare/{base}...{head}\", \"merges_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/merges\", \"archive_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/{archive_format}{/ref}\", \"downloads_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/downloads\", \"issues_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/issues{/number}\", \"pulls_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/pulls{/number}\", \"milestones_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/milestones{/number}\", \"notifications_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/notifications{?since,all,participating}\", \"labels_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/labels{/name}\", \"releases_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/releases{/id}\", \"deployments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/deployments\", \"created_at\": \"2024-02-08T06:22:28Z\", \"updated_at\": \"2024-02-08T06:23:38Z\", \"pushed_at\": \"2024-03-18T08:21:36Z\", \"git_url\": \"git://github.com/LZTD1/tinkoff_edu_2.git\", \"ssh_url\": \"git@github.com:LZTD1/tinkoff_edu_2.git\", \"clone_url\": \"https://github.com/LZTD1/tinkoff_edu_2.git\", \"svn_url\": \"https://github.com/LZTD1/tinkoff_edu_2\", \"homepage\": null, \"size\": 206, \"stargazers_count\": 0, \"watchers_count\": 0, \"language\": \"Java\", \"has_issues\": true, \"has_projects\": true, \"has_downloads\": true, \"has_wiki\": true, \"has_pages\": false, \"has_discussions\": false, \"forks_count\": 0, \"mirror_url\": null, \"archived\": false, \"disabled\": false, \"open_issues_count\": 1, \"license\": null, \"allow_forking\": true, \"is_template\": false, \"web_commit_signoff_required\": false, \"topics\": [ ], \"visibility\": \"public\", \"forks\": 0, \"open_issues\": 1, \"watchers\": 0, \"default_branch\": \"master\" } }, \"_links\": { \"self\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/pulls/5\" }, \"html\": { \"href\": \"https://github.com/LZTD1/tinkoff_edu_2/pull/5\" }, \"issue\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/issues/5\" }, \"comments\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/issues/5/comments\" }, \"review_comments\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/pulls/5/comments\" }, \"review_comment\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/pulls/comments{/number}\" }, \"commits\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/pulls/5/commits\" }, \"statuses\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2/statuses/e9e0e98c7562c2ec47d6ec23eb2762ddb295cdf3\" } }, \"author_association\": \"OWNER\", \"auto_merge\": null, \"active_lock_reason\": null } ]";
        configureFor("localhost", 3000);
        stubFor(get(urlEqualTo("/repos/LZTD1/tinkoff_edu_2/pulls"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
                    .withBody(jsonString)
            )
        );

        var jsonString2 =
            "[ { \"sha\": \"6b99dea062f28e723b2022e0aa0e3d17e05f6566\", \"node_id\": \"C_kwDOJcbWa9oAKDZiOTlkZWEwNjJmMjhlNzIzYjIwMjJlMGFhMGUzZDE3ZTA1ZjY1NjY\", \"commit\": { \"author\": { \"name\": \"LZTD1\", \"email\": \"danil227pavlov@gmail.com\", \"date\": \"2023-04-28T09:28:25Z\" }, \"committer\": { \"name\": \"LZTD1\", \"email\": \"danil227pavlov@gmail.com\", \"date\": \"2023-04-28T09:28:25Z\" }, \"message\": \"commmiiiiit!\", \"tree\": { \"sha\": \"3406bed9a73f0ed880647ed679dd59beb2094325\", \"url\": \"https://api.github.com/repos/LZTD1/Voronezh-Hakaton/git/trees/3406bed9a73f0ed880647ed679dd59beb2094325\" }, \"url\": \"https://api.github.com/repos/LZTD1/Voronezh-Hakaton/git/commits/6b99dea062f28e723b2022e0aa0e3d17e05f6566\", \"comment_count\": 0, \"verification\": { \"verified\": false, \"reason\": \"unsigned\", \"signature\": null, \"payload\": null } }, \"url\": \"https://api.github.com/repos/LZTD1/Voronezh-Hakaton/commits/6b99dea062f28e723b2022e0aa0e3d17e05f6566\", \"html_url\": \"https://github.com/LZTD1/Voronezh-Hakaton/commit/6b99dea062f28e723b2022e0aa0e3d17e05f6566\", \"comments_url\": \"https://api.github.com/repos/LZTD1/Voronezh-Hakaton/commits/6b99dea062f28e723b2022e0aa0e3d17e05f6566/comments\", \"author\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"committer\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"parents\": [ ] } ]";
        configureFor("localhost", 3000);
        stubFor(get(urlEqualTo("/repos/LZTD1/Voronezh-Hakaton/commits"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withStatus(200)
                    .withBody(jsonString2)
            )
        );

    }

}
