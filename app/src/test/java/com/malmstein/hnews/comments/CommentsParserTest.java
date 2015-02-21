package com.malmstein.hnews.comments;

import android.content.ContentValues;

import com.malmstein.hnews.json.CommentsJson;

import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class CommentsParserTest {

    private static final String BASE_URI = "https://news.ycombinator.com/news";

    private static final long storyId = 9085563;
    CommentsParser askStoryCommentsParser;
    CommentsParser storyCommentsParser;

    @Before
    public void setUp() throws Exception {
        Document askStoryComments = Jsoup.parse(CommentsJson.askStoryComments, BASE_URI);
        Document storyComments = Jsoup.parse(CommentsJson.newsComments, BASE_URI);

        askStoryCommentsParser = new CommentsParser(storyId, askStoryComments);
        storyCommentsParser = new CommentsParser(storyId, storyComments);
    }

//    @org.junit.Test
//    public void returnsAllStoryComments() {
//        Vector<ContentValues> stories = storyCommentsParser.parse();
//        assertEquals(stories.size(), 30);
//    }

    @org.junit.Test
    public void returnsAllAskStoryComments() {
        Vector<ContentValues> stories = askStoryCommentsParser.parse();
        assertEquals(stories.size(), 30);
    }

}
