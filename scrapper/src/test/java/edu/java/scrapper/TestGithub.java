package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.GithubClient;
import edu.java.clients.dto.githubDto.commit.CommitsDto;
import edu.java.clients.dto.githubDto.commit.UserDto;
import edu.java.clients.dto.githubDto.commit.commit.CommitDto;
import edu.java.clients.dto.githubDto.commit.commit.EntityDto;
import edu.java.clients.dto.githubDto.pull.Actor;
import edu.java.clients.dto.githubDto.pull.Head;
import edu.java.clients.dto.githubDto.pull.MergedBy;
import edu.java.clients.dto.githubDto.pull.Payload;
import edu.java.clients.dto.githubDto.pull.PullDto;
import edu.java.clients.dto.githubDto.pull.PullRequest;
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

    public static final List<PullDto> IDEAL_RESPONSE_PULL = List.of(
        new PullDto() {{
            setType("CreateEvent");
            setPayload(new Payload() {{
                setRef("hw5_optional");
                setMasterBranch("master");
            }});
            setActor(new Actor() {{
                setLogin("LZTD1");
            }});
        }},
        new PullDto() {{
            setType("PullRequestEvent");
            setPayload(new Payload() {{
                setAction("closed");
                setPullRequest(new PullRequest() {{
                    setComments(1);
                    setCommits(4);
                    setReviewComments(0);
                    setMergedBy(new MergedBy() {{
                        setLogin("LZTD1");
                    }});
                    setHead(new Head() {{
                        setRef("hw9");
                    }});
                }});
            }});
            setActor(new Actor() {{
                setLogin("LZTD1");
            }});
        }}

    );

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

        assertThat(response.size()).isEqualTo(2);

        assertThat(response.get(0).getType()).isEqualTo(IDEAL_RESPONSE_PULL.get(0).getType());
        assertThat(response.get(0).getPayload().getRef()).isEqualTo(IDEAL_RESPONSE_PULL.get(0).getPayload().getRef());
        assertThat(response.get(0).getPayload().getMasterBranch()).isEqualTo(IDEAL_RESPONSE_PULL.get(0).getPayload()
            .getMasterBranch());
        assertThat(response.get(0).getActor().getLogin()).isEqualTo(IDEAL_RESPONSE_PULL.get(0).getActor().getLogin());

        assertThat(response.get(1).getType()).isEqualTo(IDEAL_RESPONSE_PULL.get(1).getType());
        assertThat(response.get(1).getPayload().getAction()).isEqualTo(IDEAL_RESPONSE_PULL.get(1).getPayload()
            .getAction());
        assertThat(response.get(1).getPayload().getPullRequest().getComments()).isEqualTo(IDEAL_RESPONSE_PULL.get(1)
            .getPayload().getPullRequest().getComments());
        assertThat(response.get(1).getActor().getLogin()).isEqualTo(IDEAL_RESPONSE_PULL.get(1).getActor().getLogin());
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
            "[{ \"id\": \"36833920339\", \"type\": \"CreateEvent\", \"actor\": { \"id\": 46750499, \"login\": \"LZTD1\", \"display_login\": \"LZTD1\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?\" }, \"repo\": { \"id\": 754481646, \"name\": \"LZTD1/tinkoff_edu_2\", \"url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu_2\" }, \"payload\": { \"ref\": \"hw5_optional\", \"ref_type\": \"branch\", \"master_branch\": \"master\", \"description\": null, \"pusher_type\": \"user\" }, \"public\": true, \"created_at\": \"2024-03-24T17:11:23Z\" }, { \"id\": \"34871146722\", \"type\": \"PullRequestEvent\", \"actor\": { \"id\": 46750499, \"login\": \"LZTD1\", \"display_login\": \"LZTD1\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?\" }, \"repo\": { \"id\": 704420651, \"name\": \"LZTD1/tinkoff_edu\", \"url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu\" }, \"payload\": { \"action\": \"closed\", \"number\": 13, \"pull_request\": { \"url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/pulls/13\", \"id\": 1632230212, \"node_id\": \"PR_kwDOKfybK85hSdtE\", \"html_url\": \"https://github.com/LZTD1/tinkoff_edu/pull/13\", \"diff_url\": \"https://github.com/LZTD1/tinkoff_edu/pull/13.diff\", \"patch_url\": \"https://github.com/LZTD1/tinkoff_edu/pull/13.patch\", \"issue_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/issues/13\", \"number\": 13, \"state\": \"closed\", \"locked\": false, \"title\": \"Hw9\", \"user\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"body\": null, \"created_at\": \"2023-12-06T10:43:11Z\", \"updated_at\": \"2024-01-16T18:10:28Z\", \"closed_at\": \"2024-01-16T18:10:28Z\", \"merged_at\": \"2024-01-16T18:10:28Z\", \"merge_commit_sha\": \"b8018591baa118592e3cc995579e3ced163a7aa7\", \"assignee\": null, \"assignees\": [ ], \"requested_reviewers\": [ ], \"requested_teams\": [ ], \"labels\": [ ], \"milestone\": null, \"draft\": false, \"commits_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/pulls/13/commits\", \"review_comments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/pulls/13/comments\", \"review_comment_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/pulls/comments{/number}\", \"comments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/issues/13/comments\", \"statuses_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/statuses/abc415a112f714a6390a427bc492b88161d4632c\", \"head\": { \"label\": \"LZTD1:hw9\", \"ref\": \"hw9\", \"sha\": \"abc415a112f714a6390a427bc492b88161d4632c\", \"user\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"repo\": { \"id\": 704420651, \"node_id\": \"R_kgDOKfybKw\", \"name\": \"tinkoff_edu\", \"full_name\": \"LZTD1/tinkoff_edu\", \"private\": false, \"owner\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"html_url\": \"https://github.com/LZTD1/tinkoff_edu\", \"description\": null, \"fork\": false, \"url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu\", \"forks_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/forks\", \"keys_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/keys{/key_id}\", \"collaborators_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/collaborators{/collaborator}\", \"teams_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/teams\", \"hooks_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/hooks\", \"issue_events_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/issues/events{/number}\", \"events_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/events\", \"assignees_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/assignees{/user}\", \"branches_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/branches{/branch}\", \"tags_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/tags\", \"blobs_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/git/blobs{/sha}\", \"git_tags_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/git/tags{/sha}\", \"git_refs_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/git/refs{/sha}\", \"trees_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/git/trees{/sha}\", \"statuses_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/statuses/{sha}\", \"languages_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/languages\", \"stargazers_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/stargazers\", \"contributors_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/contributors\", \"subscribers_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/subscribers\", \"subscription_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/subscription\", \"commits_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/commits{/sha}\", \"git_commits_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/git/commits{/sha}\", \"comments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/comments{/number}\", \"issue_comment_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/issues/comments{/number}\", \"contents_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/contents/{+path}\", \"compare_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/compare/{base}...{head}\", \"merges_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/merges\", \"archive_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/{archive_format}{/ref}\", \"downloads_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/downloads\", \"issues_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/issues{/number}\", \"pulls_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/pulls{/number}\", \"milestones_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/milestones{/number}\", \"notifications_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/notifications{?since,all,participating}\", \"labels_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/labels{/name}\", \"releases_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/releases{/id}\", \"deployments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/deployments\", \"created_at\": \"2023-10-13T08:13:02Z\", \"updated_at\": \"2023-10-13T08:13:20Z\", \"pushed_at\": \"2024-01-16T18:10:28Z\", \"git_url\": \"git://github.com/LZTD1/tinkoff_edu.git\", \"ssh_url\": \"git@github.com:LZTD1/tinkoff_edu.git\", \"clone_url\": \"https://github.com/LZTD1/tinkoff_edu.git\", \"svn_url\": \"https://github.com/LZTD1/tinkoff_edu\", \"homepage\": null, \"size\": 1245, \"stargazers_count\": 0, \"watchers_count\": 0, \"language\": \"Java\", \"has_issues\": true, \"has_projects\": true, \"has_downloads\": true, \"has_wiki\": true, \"has_pages\": false, \"has_discussions\": false, \"forks_count\": 0, \"mirror_url\": null, \"archived\": false, \"disabled\": false, \"open_issues_count\": 5, \"license\": null, \"allow_forking\": true, \"is_template\": false, \"web_commit_signoff_required\": false, \"topics\": [ ], \"visibility\": \"public\", \"forks\": 0, \"open_issues\": 5, \"watchers\": 0, \"default_branch\": \"master\" } }, \"base\": { \"label\": \"LZTD1:master\", \"ref\": \"master\", \"sha\": \"c3bd17ca3894b7b4b048ad770a7b4dd798700422\", \"user\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"repo\": { \"id\": 704420651, \"node_id\": \"R_kgDOKfybKw\", \"name\": \"tinkoff_edu\", \"full_name\": \"LZTD1/tinkoff_edu\", \"private\": false, \"owner\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"html_url\": \"https://github.com/LZTD1/tinkoff_edu\", \"description\": null, \"fork\": false, \"url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu\", \"forks_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/forks\", \"keys_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/keys{/key_id}\", \"collaborators_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/collaborators{/collaborator}\", \"teams_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/teams\", \"hooks_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/hooks\", \"issue_events_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/issues/events{/number}\", \"events_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/events\", \"assignees_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/assignees{/user}\", \"branches_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/branches{/branch}\", \"tags_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/tags\", \"blobs_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/git/blobs{/sha}\", \"git_tags_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/git/tags{/sha}\", \"git_refs_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/git/refs{/sha}\", \"trees_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/git/trees{/sha}\", \"statuses_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/statuses/{sha}\", \"languages_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/languages\", \"stargazers_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/stargazers\", \"contributors_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/contributors\", \"subscribers_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/subscribers\", \"subscription_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/subscription\", \"commits_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/commits{/sha}\", \"git_commits_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/git/commits{/sha}\", \"comments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/comments{/number}\", \"issue_comment_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/issues/comments{/number}\", \"contents_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/contents/{+path}\", \"compare_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/compare/{base}...{head}\", \"merges_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/merges\", \"archive_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/{archive_format}{/ref}\", \"downloads_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/downloads\", \"issues_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/issues{/number}\", \"pulls_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/pulls{/number}\", \"milestones_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/milestones{/number}\", \"notifications_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/notifications{?since,all,participating}\", \"labels_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/labels{/name}\", \"releases_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/releases{/id}\", \"deployments_url\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/deployments\", \"created_at\": \"2023-10-13T08:13:02Z\", \"updated_at\": \"2023-10-13T08:13:20Z\", \"pushed_at\": \"2024-01-16T18:10:28Z\", \"git_url\": \"git://github.com/LZTD1/tinkoff_edu.git\", \"ssh_url\": \"git@github.com:LZTD1/tinkoff_edu.git\", \"clone_url\": \"https://github.com/LZTD1/tinkoff_edu.git\", \"svn_url\": \"https://github.com/LZTD1/tinkoff_edu\", \"homepage\": null, \"size\": 1245, \"stargazers_count\": 0, \"watchers_count\": 0, \"language\": \"Java\", \"has_issues\": true, \"has_projects\": true, \"has_downloads\": true, \"has_wiki\": true, \"has_pages\": false, \"has_discussions\": false, \"forks_count\": 0, \"mirror_url\": null, \"archived\": false, \"disabled\": false, \"open_issues_count\": 5, \"license\": null, \"allow_forking\": true, \"is_template\": false, \"web_commit_signoff_required\": false, \"topics\": [ ], \"visibility\": \"public\", \"forks\": 0, \"open_issues\": 5, \"watchers\": 0, \"default_branch\": \"master\" } }, \"_links\": { \"self\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/pulls/13\" }, \"html\": { \"href\": \"https://github.com/LZTD1/tinkoff_edu/pull/13\" }, \"issue\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/issues/13\" }, \"comments\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/issues/13/comments\" }, \"review_comments\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/pulls/13/comments\" }, \"review_comment\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/pulls/comments{/number}\" }, \"commits\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/pulls/13/commits\" }, \"statuses\": { \"href\": \"https://api.github.com/repos/LZTD1/tinkoff_edu/statuses/abc415a112f714a6390a427bc492b88161d4632c\" } }, \"author_association\": \"OWNER\", \"auto_merge\": null, \"active_lock_reason\": null, \"merged\": true, \"mergeable\": null, \"rebaseable\": null, \"mergeable_state\": \"unknown\", \"merged_by\": { \"login\": \"LZTD1\", \"id\": 46750499, \"node_id\": \"MDQ6VXNlcjQ2NzUwNDk5\", \"avatar_url\": \"https://avatars.githubusercontent.com/u/46750499?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/LZTD1\", \"html_url\": \"https://github.com/LZTD1\", \"followers_url\": \"https://api.github.com/users/LZTD1/followers\", \"following_url\": \"https://api.github.com/users/LZTD1/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/LZTD1/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/LZTD1/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/LZTD1/subscriptions\", \"organizations_url\": \"https://api.github.com/users/LZTD1/orgs\", \"repos_url\": \"https://api.github.com/users/LZTD1/repos\", \"events_url\": \"https://api.github.com/users/LZTD1/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/LZTD1/received_events\", \"type\": \"User\", \"site_admin\": false }, \"comments\": 1, \"review_comments\": 0, \"maintainer_can_modify\": false, \"commits\": 4, \"additions\": 673, \"deletions\": 0, \"changed_files\": 21 } }, \"public\": true, \"created_at\": \"2024-01-16T18:10:29Z\" } ]";

        configureFor("localhost", 3000);
        stubFor(get(urlEqualTo("/repos/LZTD1/tinkoff_edu_2/events"))
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
