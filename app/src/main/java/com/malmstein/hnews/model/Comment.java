package com.malmstein.hnews.model;

import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.malmstein.hnews.data.HNewsContract;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Comment extends Item {

    private final Long parent;
    private final String text;
    private final ArrayList<String> kids;

    public Comment(Long internalId, String by, Long id, String type, Long time, Long parent, String text, ArrayList<String> kids) {
        super(internalId, by, id, type, time);
        this.parent = parent;
        this.text = text;
        this.kids = kids;
    }

    public Long getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getKids() {
        return kids;
    }

    public static Comment from(Cursor cursor) {
        Long internalId = cursor.getLong(HNewsContract.COLUMN_ID);
        Long id = cursor.getLong(HNewsContract.COLUMN_ITEM_ID);
        String by = cursor.getString(HNewsContract.COLUMN_BY);
        long time = cursor.getLong(HNewsContract.COLUMN_TIME);
        String type = cursor.getString(HNewsContract.COLUMN_TYPE);

        Long parent = cursor.getLong(HNewsContract.COLUMN_PARENT);
        String text = cursor.getString(HNewsContract.COLUMN_TEXT);
        String kids = cursor.getString(HNewsContract.COLUMN_KIDS);

        Gson gson = new Gson();
        Type jsonType = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> kidsArray = gson.fromJson(kids, jsonType);

        return new Comment(internalId, by, id, type, time, parent, text, kidsArray);
    }
}

//{
//        "by":"norvig",
//        "id":2921983,
//        "kids":[2922097,2922429,2924562,2922709,2922573,2922140,2922141],
//        "parent":2921506,
//        "text":"Aw shucks, guys ... you make me blush with your compliments.<p>Tell you what, Ill make a deal: I'll keep writing if you keep reading. K?",
//        "time":1314211127,
//        "type":"comment"
//        }