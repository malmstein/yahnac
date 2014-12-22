package com.malmstein.hnews.tasks;

import com.malmstein.hnews.comments.CommentsParser;
import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.http.ConnectionProvider;

import java.io.IOException;

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

    private final DatabasePersister databasePersister;
    private final ConnectionProvider connectionProvider;
    private final Long storyId;

    public FetchCommentsTask(final DatabasePersister databasePersister, ConnectionProvider connectionProvider, Long storyId) {
        this.databasePersister = databasePersister;
        this.connectionProvider = connectionProvider;
        this.storyId = storyId;

    }

    public void execute() throws IOException {
        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParameters, getConnectionTimeout());
        HttpConnectionParams.setSoTimeout(httpParameters, getReadTimeout());

        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpGet httpget = new HttpGet("https://news.ycombinator.com/item?id=" + storyId);

        HttpResponse response = httpclient.execute(httpget);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity httpEntity = response.getEntity();

            String result = EntityUtils.toString(httpEntity);

            Document doc = Jsoup.parse(result);


            databasePersister.persistComments(new CommentsParser(storyId, doc).parse(), storyId);
        }
    }

    protected int getConnectionTimeout() {
        return DEFAULT_CONNECTION_TIMEOUT;
    }

    protected int getReadTimeout() {
        return DEFAULT_READ_TIMEOUT;
    }
}
