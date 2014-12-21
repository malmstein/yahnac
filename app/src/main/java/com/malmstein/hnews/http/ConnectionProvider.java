package com.malmstein.hnews.http;

import java.net.HttpURLConnection;
import java.net.URL;

public interface ConnectionProvider {

    HttpURLConnection open(URL url);

}
