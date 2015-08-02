package com.malmstein.yahnac.provider;

import java.io.File;
import java.net.URL;

public class TestFileProvider {

    private static File getFileFromPath(Object obj, String fileName) {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getPath());
    }

    public static File getNewsStoryComments(Object obj) {
        return getFileFromPath(obj, "res/comments_9989667.html");
    }

    public static File getAskStoryComments(Object obj) {
        return getFileFromPath(obj, "res/comments_ask_998917.html");
    }

}
