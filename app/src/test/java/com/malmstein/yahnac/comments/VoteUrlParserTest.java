package com.malmstein.yahnac.comments;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VoteUrlParserTest {

    private static final String BASE_URI = "https://news.ycombinator.com/news";
    private static final String VOTE_URL_STORY_SAMPLE = "/vote?for=9989667&dir=up&auth=d390b2aa5f729fbf4b6c8369ebb23dbc1c32304b&goto=item%3Fid%3D9989667";
    private static final String VOTE_URL_ASK_STORY_SAMPLE = "/vote?for=9996335&dir=up&auth=5fff39e5a097738db1aa4077319d1daf327c3399&goto=item%3Fid%3D9996335";

    private final Long SAMPLE_STORY = Long.valueOf(9989667);
    private final Long SAMPLE_ASK_STORY = Long.valueOf(9996335);

    VoteUrlParser askStoryVoteUrlParser;
    VoteUrlParser storyVoteUrlParser;

    @org.junit.Test
    public void fileObjectShouldNotBeNull() {
        File file = new File(getClass().getResource("/comments_9989667.html").getPath());
        assertNotNull(file);
    }

    @org.junit.Test
    public void retrieveVoteUrlFromAskStory() throws IOException {
        File file = new File(getClass().getResource("/comments_ask_998917.html").getPath());
        Document askStoryComments = Jsoup.parse(file, "UTF-8", BASE_URI);
        askStoryVoteUrlParser = new VoteUrlParser(askStoryComments, SAMPLE_ASK_STORY);
        String voteUrl = askStoryVoteUrlParser.parse();

        assertEquals(VOTE_URL_ASK_STORY_SAMPLE, voteUrl);
    }

    @org.junit.Test
    public void retrieveVoteUrlFromNewsStory() throws IOException {
        File file = new File(getClass().getResource("/comments_9989667.html").getPath());
        Document storyComments = Jsoup.parse(file, "UTF-8", BASE_URI);

        storyVoteUrlParser = new VoteUrlParser(storyComments, SAMPLE_STORY);
        String voteUrl = storyVoteUrlParser.parse();
        assertEquals(VOTE_URL_STORY_SAMPLE, voteUrl);
    }

}
