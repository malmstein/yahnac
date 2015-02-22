package com.malmstein.hnews.model;

import android.content.ContentValues;

import java.util.Vector;

public class StoriesJsoup {

    private Vector<ContentValues> stories = new Vector<>();
    private String nextUrl;

    public StoriesJsoup(Vector<ContentValues> stories, String nextUrl) {
        this.stories = stories;
        this.nextUrl = nextUrl;
    }

    public static StoriesJsoup empty() {
        return new StoriesJsoup(new Vector<ContentValues>(), "");
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public Vector<ContentValues> getStories() {
        return stories;
    }
}
