package com.malmstein.yahnac.comments;

import android.content.ContentValues;

import com.malmstein.yahnac.json.CommentsJson;

import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class CommentsParserTest {

    private static final String BASE_URI = "https://news.ycombinator.com/news";

    private static final String TITLE_SAMPLE = "A couple of comments. Sorry you were let go, but that is just business, they need people with more experience and likely learned the hard way that they can't pay lower prices for less experience and expect things to work out. That really isn't a reflection on you or your abilities so much as it is on them for bad planning. So don't look at this as you failed them as much as they failed you and likely others.<p>As for what you do next. Take a few days and chill and have some fun, spend a little time brushing up your resume and start sending them out. There are a lot of positions available. Remember this, when you are applying for jobs you are selling yourself and what you can do for the company. So many times I interview developers and they don't realize that they need to sell themselves. I am not talking about being a sleazy used car salesman, but recognizing you have to tell people what you are capable of and be able to back it up with mastery of some set of skills.</p><p>Last point, you mentioned you are to early to have specialized. I totally disagree with this approach but others may have different opinions. I think you should specialize in a couple of key areas, generally for someone with 1-2 years experience I expect them to know a single language quite well, have a little knowledge of others and be able to do some data queries in SQL (or the database they have been using) etc. I want new hires to have some depth not as much breadth, it is important to get a wide span of knowledge, but it is even more important to go deep in a couple of areas. You have plenty of time to master multiple skills, but focus first as it makes you more valuable. For a web dev, I'd expect decent JS, css, and whatever their front end language is to be fairly mastered in terms of syntax and core coding. I wouldn't expect advanced topics like concurrency or nuances of debugging yet (unless they have been heavily mentored).</p><p>Good luck, you should have plenty of runway depending on where you are at in the country, and whether you are willing to relocate.</p>";
    private static final String AUTHOR_SAMPLE = "davismwfl";
    private static final String TIME_AGO_SAMPLE = "24 minutes ago";
    private static final String QUESTION_SAMPLE = "I'm a student developer, @pravj on GitHub. I try to follow the best possible development practice when I'm working like documentation, respecting language's style guide etc.<p>But one thing I don't like with me is that I never write tests.</p><p>Why I never followed that aspect is because I always felt that the thing that I'm writing, sounds totally OK.</p><p>One another negative point is that I don't actually get it that How to do/start this and what type of tests I should write for any given project. Maybe this is because I have never done this before.</p><p>For example recently I wrote a identicons library, Penticons[1]. It generates GitHub contribution flavored identicons. You can read the development story here[2].</p><p>Now, the thing that stopped me from writing tests was, I thought that ultimately it's a image generation library, why this thing needs any tests.</p><p>I don't want to lost my 1 point in the Joel score. Whenever I see a <i>build passing</i> label on a GitHub repository, It makes me crazy.</p><p>I'm asking here because I just love HN, I know people will suggest some awesome things here.</p><p>1. https://github.com/penticons/penticons.go</p><p>2. http://pravj.github.io/blog/penticons-the-hash-game/</p>";
    private static final String EMPTY_QUESTION_SAMPLE = "";

    private static final long storyId = 9085563;

    CommentsParser askStoryCommentsParser;
    CommentsParser storyCommentsParser;

    Element topRowElement;
    Element secondRowElement;

    @Before
    public void setUp() throws Exception {
        Document askStoryComments = Jsoup.parse(CommentsJson.askStoryComments, BASE_URI);
        Document storyComments = Jsoup.parse(CommentsJson.newsComments, BASE_URI);

        Elements tableRows = askStoryComments.select("table tr table tr:has(table)");

        topRowElement = tableRows.get(0).select("td:eq(0)").first();
        secondRowElement = tableRows.get(0).select("td:eq(0)").first();

        askStoryCommentsParser = new CommentsParser(storyId, askStoryComments);
        storyCommentsParser = new CommentsParser(storyId, storyComments);
    }

    @org.junit.Test
    public void returnsAllStoryComments() {
        Vector<ContentValues> storyComments = storyCommentsParser.parse();
        assertEquals(15, storyComments.size());
    }

    @org.junit.Test
    public void returnsAllAskStoryComments() {
        Vector<ContentValues> storyComments = askStoryCommentsParser.parse();
        assertEquals(12, storyComments.size());
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

}
