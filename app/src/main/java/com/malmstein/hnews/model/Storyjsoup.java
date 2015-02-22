package com.malmstein.hnews.model;

import android.content.ContentValues;

import java.util.Vector;

public class Storyjsoup {

    private Vector<ContentValues> stories;
    private String nextUrl;

    public Storyjsoup(Vector<ContentValues> stories, String nextUrl) {
        this.stories = stories;
        this.nextUrl = nextUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public Vector<ContentValues> getStories() {
        return stories;
    }
}
