package com.malmstein.yahnac.comments;

import com.malmstein.yahnac.provider.TestFileProvider;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VoteUrlParserTest {

    private static final String BASE_URI = "https://news.ycombinator.com/news";

    private static final String VOTE_URL_SAMPLE = "/vote?for=9961839&dir=up&auth=65320dedf2defa8de93e369a54f8236cdb883ab1&goto=item%3Fid%3D9961723";

    VoteUrlParser askStoryVoteUrlParser;
    VoteUrlParser storyVoteUrlParser;

    Element voteElement;

    @org.junit.Test
    public void fileObjectShouldNotBeNull() throws Exception {
        File file = TestFileProvider.getNewsStoryComments(this);
        assertNotNull(file);
    }

    @org.junit.Test
    public void retrieveVoteUrlFromAskStory() {
        File file = TestFileProvider.getAskStoryComments(this);
        Document askStoryComments = Jsoup.parse(file.toString(), BASE_URI);
        askStoryVoteUrlParser = new VoteUrlParser(askStoryComments);
        String voteUrl = askStoryVoteUrlParser.parse();

        assertEquals(VOTE_URL_SAMPLE, voteUrl);
    }

    @org.junit.Test
    public void retrieveVoteUrlFromNewsStory() {
        File file = TestFileProvider.getNewsStoryComments(this);
        Document storyComments = Jsoup.parse(file.toString());

        storyVoteUrlParser = new VoteUrlParser(storyComments);
        String voteUrl = storyVoteUrlParser.parseVoteUrl(voteElement);
        assertEquals(VOTE_URL_SAMPLE, voteUrl);
    }

}
