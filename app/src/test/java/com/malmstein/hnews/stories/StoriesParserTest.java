package com.malmstein.hnews.stories;

import com.malmstein.hnews.json.StoriesJson;
import com.malmstein.hnews.model.StoriesJsoup;
import com.malmstein.hnews.model.Story;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class StoriesParserTest {

    private static final String EMPTY = "";
    private static final int ZERO = 0;

    private static final String NEWS_STORY_TITLE = "Vaccine opposition has ebbed and flowed over centuries";
    private static final String NEWS_STORY_DOMAIN = "(komonews.com)";
    private static final String NEWS_STORY_POINTS = "1";
    private static final String NEWS_STORY_URL = "http://www.komonews.com/news/health/Vaccine-opposition-has-ebbed-and-flowed-over-centuries-291956021.html";
    private static final String NEWS_STORY_SUBMITTER = "Mz";
    private static final String NEWS_STORY_TIME_AGO = "0 minutes ago";
    private static final int NEWS_STORY_COMMENTS = 0;
    private static final int NEWS_STORY_ID = 9050291;

    private static final String JOBS_STORY_TITLE = "Aptible (YC S14) Is Hiring Devops Platform Engineers";
    private static final String JOBS_STORY_DOMAIN = "(lever.co)";
    private static final String JOBS_STORY_URL = "https://jobs.lever.co/aptible/e14de4f6-9fb1-426d-8003-82b91f72d1f9?lever-source=HackerNews";
    private static final String JOBS_STORY_TIME_AGO = "5 hours ago";

    private static final String NEXT_PAGE_SAMPLE = "https://news.ycombinator.com/newest?next=9050123&n=31";

    private static final String BASE_URI = "https://news.ycombinator.com/news";
    private static final Story.TYPE DEFAULT_TYPE = Story.TYPE.top_story;

    StoriesParser storiesNewsParser;
    StoriesParser storiesJobsParser;

    Element newsFirstLine;
    Element newsSecondLine;
    Element jobsFirstLine;
    Element jobsSecondLine;

    @Before
    public void setUp() throws Exception {
        Document jobsDoc = Jsoup.parse(StoriesJson.jobs, BASE_URI);
        Document newsDoc = Jsoup.parse(StoriesJson.new_stories, BASE_URI);

        newsFirstLine = newsDoc.select("body>center>table>tbody>tr>td>table>tbody>tr:has(td[class=title])").get(1);
        newsSecondLine = newsDoc.select("body>center>table>tbody>tr>td>table>tbody>tr:has(td[class=subtext])").get(1);

        jobsFirstLine = jobsDoc.select("body>center>table>tbody>tr>td>table>tbody>tr:has(td[class=title])").get(1);
        jobsSecondLine = jobsDoc.select("body>center>table>tbody>tr>td>table>tbody>tr:has(td[class=subtext])").get(1);

        storiesNewsParser = new StoriesParser(newsDoc);
        storiesJobsParser = new StoriesParser(jobsDoc);
    }

    @org.junit.Test
    public void returnsAllNews() {
        StoriesJsoup stories = storiesNewsParser.parse(DEFAULT_TYPE);
        assertEquals(stories.getStories().size(), 30);
    }

    @org.junit.Test
    public void returnsAllJobs() {
        StoriesJsoup stories = storiesNewsParser.parse(DEFAULT_TYPE);
        assertEquals(stories.getStories().size(), 30);
    }

    @org.junit.Test
    public void returnsNewsTitle() {
        String title = storiesNewsParser.parseTitle(newsFirstLine);
        assertEquals(NEWS_STORY_TITLE, title);
    }

    @org.junit.Test
    public void returnsJobsTitle() {
        String title = storiesJobsParser.parseTitle(jobsFirstLine);
        assertEquals(JOBS_STORY_TITLE, title);
    }

    @org.junit.Test
    public void returnsNewsDomain() {
        String domain = storiesNewsParser.parseDomain(newsFirstLine);
        assertEquals(NEWS_STORY_DOMAIN, domain);
    }

    @org.junit.Test
    public void returnsJobsDomain() {
        String domain = storiesJobsParser.parseDomain(jobsFirstLine);
        assertEquals(JOBS_STORY_DOMAIN, domain);
    }

    @org.junit.Test
    public void returnsNewsPoints() {
        String points = storiesNewsParser.parsePoints(newsSecondLine);
        assertEquals(NEWS_STORY_POINTS, points);
    }

    @org.junit.Test
    public void returnEmptyJobsPoints() {
        String points = storiesJobsParser.parsePoints(jobsSecondLine);
        assertEquals(EMPTY, points);
    }

    @org.junit.Test
    public void returnsNewsUrl() {
        String points = storiesNewsParser.parseUrl(newsFirstLine);
        assertEquals(NEWS_STORY_URL, points);
    }

    @org.junit.Test
    public void returnJobsUrl() {
        String points = storiesJobsParser.parseUrl(jobsFirstLine);
        assertEquals(JOBS_STORY_URL, points);
    }

    @org.junit.Test
    public void returnsNewsSubmitter() {
        String submitter = storiesNewsParser.parseSubmitter(newsSecondLine);
        assertEquals(NEWS_STORY_SUBMITTER, submitter);
    }

    @org.junit.Test
    public void returnEmptyJobsSubmitter() {
        String submitter = storiesJobsParser.parseSubmitter(jobsSecondLine);
        assertEquals(EMPTY, submitter);
    }

    @org.junit.Test
    public void returnNewsTimeAgo() {
        String timeAgo = storiesNewsParser.parseAgo(newsSecondLine);
        assertEquals(NEWS_STORY_TIME_AGO, timeAgo);
    }

    @org.junit.Test
    public void returnJobsTimeAgo() {
        String timeAgo = storiesJobsParser.parseAgo(jobsSecondLine);
        assertEquals(JOBS_STORY_TIME_AGO, timeAgo);
    }

    @org.junit.Test
    public void returnNewsComments() {
        int comments = storiesNewsParser.parseComments(newsSecondLine);
        assertEquals(NEWS_STORY_COMMENTS, comments);
    }

    @org.junit.Test
    public void returnNoJobsComments() {
        int comments = storiesJobsParser.parseComments(jobsSecondLine);
        assertEquals(ZERO, comments);
    }

    @org.junit.Test
    public void returnNewsItemId() {
        int itemId = storiesNewsParser.parseItemId(newsSecondLine);
        assertEquals(NEWS_STORY_ID, itemId);
    }

    @org.junit.Test
    public void returnJobsItemId() {
        int itemId = storiesJobsParser.parseItemId(jobsSecondLine);
        assertEquals(ZERO, itemId);
    }

    @org.junit.Test
    public void returnNextPageUrl() {
        String nextPageUrl = storiesNewsParser.parseNextPageUrl();
        assertEquals(NEXT_PAGE_SAMPLE, nextPageUrl);
    }
}