package com.malmstein.hnews.tasks;

import android.content.ContentValues;

import com.malmstein.hnews.comments.CommentsParser;
import com.malmstein.hnews.model.Story;

import java.io.IOException;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetchCommentsTask {

    private static final int DEFAULT_CONNECTION_TIMEOUT = 15 * 1000;
    private static final int DEFAULT_READ_TIMEOUT = 15 * 1000;

    private final Long storyId;

    public FetchCommentsTask(Long storyId) {
        this.storyId = storyId;
    }

    public Vector<ContentValues> execute() throws IOException {
        Vector<ContentValues> commentsList = new Vector<>();

        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParameters, getConnectionTimeout());
        HttpConnectionParams.setSoTimeout(httpParameters, getReadTimeout());

        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpGet httpget = new HttpGet(Story.COMMENT_URL_BASE + storyId);

        HttpResponse response = httpclient.execute(httpget);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity httpEntity = response.getEntity();

            String result = EntityUtils.toString(httpEntity);

            Document doc = Jsoup.parse(result);

            commentsList = new CommentsParser(storyId, doc).parse();
        }
        return commentsList;
    }

    protected int getConnectionTimeout() {
        return DEFAULT_CONNECTION_TIMEOUT;
    }

    protected int getReadTimeout() {
        return DEFAULT_READ_TIMEOUT;
    }
}
