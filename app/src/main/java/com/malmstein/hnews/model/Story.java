package com.malmstein.hnews.model;

import android.database.Cursor;

import com.malmstein.hnews.data.HNewsContract;

import java.io.Serializable;
import java.util.ArrayList;

public class Story extends Item implements Serializable {

    private final int score;
    private final String title;
    private final String url;
    private final ArrayList<String> kids;

    public Story(Long internalId, String by, Long id, String type, Long time, int score, String title, String url, ArrayList<String> kids, Long updated) {
        super(internalId, by, id, type, time, updated);
        this.score = score;
        this.title = title;
        this.url = url;
        this.kids = kids;
    }

    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public static Story from(Cursor cursor) {
        Long internalId = cursor.getLong(HNewsContract.COLUMN_ID);
        String by = cursor.getString(HNewsContract.COLUMN_BY);
        Long id = cursor.getLong(HNewsContract.COLUMN_ITEM_ID);
        int score = cursor.getInt(HNewsContract.COLUMN_SCORE);
        Long time = cursor.getLong(HNewsContract.COLUMN_TIME);
        String title = cursor.getString(HNewsContract.COLUMN_TITLE);
        String type = cursor.getString(HNewsContract.COLUMN_TYPE);
        String url = cursor.getString(HNewsContract.COLUMN_URL);
        Long updated = cursor.getLong(HNewsContract.COLUMN_UPDATED);
        String kids = cursor.getString(HNewsContract.COLUMN_KIDS);

//        Gson gson = new Gson();
//        Type jsonType = new TypeToken<ArrayList<String>>() {}.getType();
//        ArrayList<String> kidsList = gson.fromJson(kids, jsonType);

        return new Story(internalId, by, id, type, time, score, title, url, new ArrayList<String>(), updated);
    }

    public ArrayList<String> getKids() {
        return kids;
    }
}

//    "by":"dhouston",
//            "id":8863,
//            "kids":[8952,9224,8917,8884,8887,8943,8869,8958,9005,9671,8940,9067,8908,9055,8865,8881,8872,8873,8955,10403,8903,8928,9125,8998,8901,8902,8907,8894,8878,8870,8980,8934,8876],
//            "score":111,
//            "time":1175714200,
//            "title":"My YC app: Dropbox - Throw away your USB drive",
//            "type":"story",
//            "url":"http://www.getdropbox.com/u/2/screencast.html"