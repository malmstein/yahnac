package com.malmstein.yahnac.comments;

import com.malmstein.yahnac.comments.parser.VoteUrlParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VoteUrlParserTest {

    private static final String BASE_URI = "https://news.ycombinator.com/news";
    private static final String VOTE_URL_STORY_SAMPLE = "/vote?id=12423794&how=up&auth=cc66a84d11061fa3d32103b4ad57e64f48b0ec7a&goto=item%3Fid%3D12423794";
    private static final String VOTE_URL_ASK_STORY_SAMPLE = "/vote?for=9996335&dir=up&auth=5fff39e5a097738db1aa4077319d1daf327c3399&goto=item%3Fid%3D9996335";

    private final Long SAMPLE_STORY = Long.valueOf(12423794);
    private final Long SAMPLE_ASK_STORY = Long.valueOf(9996335);

    VoteUrlParser askStoryVoteUrlParser;
    VoteUrlParser storyVoteUrlParser;

    @Before
    public void setUp() throws Exception {
        File askStoryFile = new File(getClass().getResource("/comments_ask_998917.html").getPath());
        Document askStoryComments = Jsoup.parse(askStoryFile, "UTF-8", BASE_URI);
        askStoryVoteUrlParser = new VoteUrlParser(askStoryComments, SAMPLE_ASK_STORY);

        File storyFile = new File(getClass().getResource("/comments_9989667.html").getPath());
        Document storyComments = Jsoup.parse(storyFile, "UTF-8", BASE_URI);
        storyVoteUrlParser = new VoteUrlParser(storyComments, SAMPLE_STORY);
    }

    @org.junit.Test
    public void fileObjectShouldNotBeNull() {
        File file = new File(getClass().getResource("/comments_9989667.html").getPath());
        assertNotNull(file);
    }

    @org.junit.Test
    public void retrieveVoteUrlFromAskStory() throws IOException {
        String voteUrl = askStoryVoteUrlParser.parse();
        assertEquals(VOTE_URL_ASK_STORY_SAMPLE, voteUrl);
    }

    @org.junit.Test
    public void retrieveVoteUrlFromNewsStory() throws IOException {
        String voteUrl = storyVoteUrlParser.parse();
        assertEquals(VOTE_URL_STORY_SAMPLE, voteUrl);
    }

}
