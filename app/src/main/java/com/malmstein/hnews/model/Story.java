package com.malmstein.hnews.model;

public class Story extends Item {

    private final int score;
    private final String title;
    private final String url;

    public Story(String by, int id, String type, Long time, int score, String title, String url) {
        super(by, id, type, time);
        this.score = score;
        this.title = title;
        this.url = url;
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
}

//    "by":"dhouston",
//            "id":8863,
//            "kids":[8952,9224,8917,8884,8887,8943,8869,8958,9005,9671,8940,9067,8908,9055,8865,8881,8872,8873,8955,10403,8903,8928,9125,8998,8901,8902,8907,8894,8878,8870,8980,8934,8876],
//            "score":111,
//            "time":1175714200,
//            "title":"My YC app: Dropbox - Throw away your USB drive",
//            "type":"story",
//            "url":"http://www.getdropbox.com/u/2/screencast.html"