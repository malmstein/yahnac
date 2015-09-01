package com.malmstein.yahnac.data;

import com.malmstein.yahnac.data.updater.LoginSharedPreferences;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class ConnectionProvider {

    public static final String BASE_URL = "https://news.ycombinator.com";

    public static final String LOGIN_URL_EXTENSION = "/login?go_to=news";
    public static final String LOGIN_BASE_URL = "/login";

    public static final String ITEM_BASE_URL = "/item?id=";

    public static final String REPLY_BASE_URL = "/reply?id=";
    public static final String REPLY_GOTO = "&goto=item%3Fid%3D";

    public static final String SEND_COMMENT_BASE_URL = "/comment";

    public static final String USER_AGENT = System.getProperty("http.agent");
    public static final int TIMEOUT_MILLIS = 40 * 1000;

    private final LoginSharedPreferences loginSharedPreferences;

    public ConnectionProvider() {
        this.loginSharedPreferences = LoginSharedPreferences.newInstance().newInstance();
    }

    private static Connection defaultConnection(String baseUrlExtension) {
        Connection conn = Jsoup.connect(BASE_URL + baseUrlExtension)
                .timeout(TIMEOUT_MILLIS)
                .userAgent(USER_AGENT);
        conn.header("Accept-Encoding", "gzip");

        return conn;
    }

    private static Connection authorisedConnection(String baseUrlExtension, String userCookie) {
        return defaultConnection(baseUrlExtension).cookie("user", userCookie);
    }

    private Connection connection(String baseUrlExtension) {
        if (loginSharedPreferences.isLoggedIn()) {
            return authorisedConnection(baseUrlExtension, loginSharedPreferences.getCookie());
        } else {
            return defaultConnection(baseUrlExtension);
        }
    }

    public Connection commentsConnection(Long storyId) {
        return connection(ITEM_BASE_URL + storyId);
    }

    public Connection loginConnection(String username, String password) {
        Connection login = connection(LOGIN_BASE_URL);
        return login
                .data("go_to", "news")
                .data("acct", username)
                .data("pw", password)
                .header("Origin", ConnectionProvider.BASE_URL)
                .followRedirects(true)
                .referrer(ConnectionProvider.BASE_URL + ConnectionProvider.LOGIN_URL_EXTENSION)
                .method(Connection.Method.POST);

    }

    public Connection voteConnection(String voteUrl) {
        return connection(voteUrl);
    }

    public Connection replyCommentConnection(Long storyId, Long commentId) {
        return connection(REPLY_BASE_URL + commentId + REPLY_GOTO + storyId);
    }

    public Request commentOnStoryRequest(String itemId, String comment, String hmac) {
        RequestBody requestBody = (new FormEncodingBuilder())
                .add("parent", itemId)
                .add("goto", (new StringBuilder()).append("item?id=").append(itemId).toString())
                .add("text", comment)
                .add("hmac", hmac)
                .build();

        return createAuthRequest(requestBody);
    }

    public Request replyToCommentRequest(String itemId, String commentId, String comment, String hmac) {
        RequestBody requestBody = (new FormEncodingBuilder())
                .add("parent", commentId)
                .add("goto", (new StringBuilder()).append("item?id=").append(itemId).toString())
                .add("text", comment)
                .add("hmac", hmac)
                .build();

        return createAuthRequest(requestBody);
    }

    private Request createAuthRequest(RequestBody requestBody) {
        return (new Request.Builder())
                .addHeader("cookie", (new StringBuilder()).append("user=").append(loginSharedPreferences.getCookie()).toString())
                .url(ConnectionProvider.BASE_URL + ConnectionProvider.SEND_COMMENT_BASE_URL)
                .post(requestBody)
                .build();
    }
}

