package com.malmstein.hnews.http;

import com.squareup.okhttp.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.net.HttpURLConnection;
import java.net.URL;

public class AndroidConnectionProvider implements ConnectionProvider {

    private final OkHttpClient client;

    AndroidConnectionProvider(OkHttpClient client) {
        this.client = client;
    }

    public static AndroidConnectionProvider newUnauthenticatedProvider() {
        OkHttpClient client = new OkHttpClient();
        client.setHostnameVerifier(new NullHostNameVerifier());
        return new AndroidConnectionProvider(client);
    }

    public static AndroidConnectionProvider newInstance() {
        return new AndroidConnectionProvider(new OkHttpClient());
    }

    @Override
    public HttpURLConnection open(URL url) {
        return client.open(url);
    }

    public static class NullHostNameVerifier implements HostnameVerifier {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
