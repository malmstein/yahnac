package com.malmstein.yahnac.comments;

import android.content.ContentValues;

import com.malmstein.yahnac.comments.parser.CommentsParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;

import java.io.File;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

public class CommentsParserTest {

    private static final String BASE_URI = "https://news.ycombinator.com/news";

    private static final String TITLE_SAMPLE = "Makes you wonder how many cool things people would have written for the native Intel GPU instruction set if there existed supported interfaces for it. All the programming information has been out there in Intel-provided open source drivers for many many years.";
    private static final String AUTHOR_SAMPLE = "fulafel";
    private static final String TIME_AGO_SAMPLE = "2 hours ago";
    private static final String QUESTION_SAMPLE = "I'm a student developer, @pravj on GitHub. I try to follow the best possible development practice when I'm working like documentation, respecting language's style guide etc.<p>But one thing I don't like with me is that I never write tests.</p><p>Why I never followed that aspect is because I always felt that the thing that I'm writing, sounds totally OK.</p><p>One another negative point is that I don't actually get it that How to do/start this and what type of tests I should write for any given project. Maybe this is because I have never done this before.</p><p>For example recently I wrote a identicons library, Penticons[1]. It generates GitHub contribution flavored identicons. You can read the development story here[2].</p><p>Now, the thing that stopped me from writing tests was, I thought that ultimately it's a image generation library, why this thing needs any tests.</p><p>I don't want to lost my 1 point in the Joel score. Whenever I see a <i>build passing</i> label on a GitHub repository, It makes me crazy.</p><p>I'm asking here because I just love HN, I know people will suggest some awesome things here.</p><p>1. https://github.com/penticons/penticons.go</p><p>2. http://pravj.github.io/blog/penticons-the-hash-game/</p>";
    private static final String EMPTY_QUESTION_SAMPLE = "";

    private static final long storyId = 9085563;

    CommentsParser askStoryCommentsParser;
    CommentsParser storyCommentsParser;

    Element topRowElement;
    Element secondRowElement;
    Element voteElement;

    @Before
    public void setUp() throws Exception {
        File newsStoryCommentsFile = new File(getClass().getResource("/comments_9989667.html").getPath());
        File askStoryCommentsFile = new File(getClass().getResource("/comments_ask_998917.html").getPath());
        Document askStoryComments = Jsoup.parse(askStoryCommentsFile, "UTF-8", BASE_URI);
        Document storyComments = Jsoup.parse(newsStoryCommentsFile, "UTF-8", BASE_URI);

        Elements tableRows = storyComments.select("table tr table tr:has(table)");

        topRowElement = tableRows.get(0).select("td:eq(2)").first();
        secondRowElement = tableRows.get(0).select("td:eq(0)").first();
        secondRowElement = tableRows.get(0).select("td:eq(0)").first();
        voteElement = tableRows.get(0).select("td:eq(1) a").first();

        askStoryCommentsParser = new CommentsParser(storyId, askStoryComments);
        storyCommentsParser = new CommentsParser(storyId, storyComments);
    }

    @org.junit.Test
    public void returnsAllStoryComments() {
        Vector<ContentValues> storyComments = storyCommentsParser.parse();
        assertEquals(3, storyComments.size());
    }

    @org.junit.Test
    public void returnsAllAskStoryComments() {
        Vector<ContentValues> storyComments = askStoryCommentsParser.parse();
        assertEquals(53, storyComments.size());
    }

    @org.junit.Test
    public void returnsCommentText() {
        String title = storyCommentsParser.parseText(topRowElement);
        assertEquals(TITLE_SAMPLE, title);
    }

    @org.junit.Test
    public void returnsCommentAuthor() {
        String author = storyCommentsParser.parseAuthor(topRowElement);
        assertEquals(AUTHOR_SAMPLE, author);
    }

    @org.junit.Test
    public void returnsCommentTimeAgo() {
        String timeAgo = storyCommentsParser.parseTimeAgo(topRowElement);
        assertEquals(TIME_AGO_SAMPLE, timeAgo);
    }

    @org.junit.Test
    public void returnsCommentLevel() {
        int level = storyCommentsParser.parseLevel(secondRowElement);
        assertEquals(0, level);
    }

    @org.junit.Test
    public void returnsCommentId() {
        String commentId = storyCommentsParser.parseCommentId(topRowElement);
        assertEquals("12424346", commentId);
    }

}
