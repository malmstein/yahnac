package com.malmstein.yahnac.comments;

import com.malmstein.yahnac.json.CommentsJson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class VoteUrlParserTest {

    private static final String BASE_URI = "https://news.ycombinator.com/news";

    private static final String VOTE_URL_SAMPLE = "/vote?for=9961839&dir=up&auth=65320dedf2defa8de93e369a54f8236cdb883ab1&goto=item%3Fid%3D9961723";

    VoteUrlParser askStoryVoteUrlParser;
    VoteUrlParser storyVoteUrlParser;

    Element voteElement;

    @Before
    public void setUp() throws Exception {
        Document askStoryComments = Jsoup.parse(CommentsJson.askStoryComments, BASE_URI);
        Document storyComments = Jsoup.parse(CommentsJson.newsComments, BASE_URI);

        Elements tableRows = askStoryComments.select("table tr table tr:has(table)");

        voteElement = tableRows.get(0).select("td:eq(1) a").first();

        askStoryVoteUrlParser = new VoteUrlParser(askStoryComments);
        storyVoteUrlParser = new VoteUrlParser(storyComments);
    }

    @org.junit.Test
    public void retrieveVoteUrl() {
        String voteUrl = storyVoteUrlParser.parseVoteUrl(voteElement);
        assertEquals(VOTE_URL_SAMPLE, voteUrl);
    }

}
