package com.malmstein.hnews.model;

public class Comment extends Item {

    private final int parent;
    private final String text;
    private final String[] kids;

    public Comment(String by, int id, String type, Long time, int parent, String text, String[] kids) {
        super(by, id, type, time);
        this.parent = parent;
        this.text = text;
        this.kids = kids;
    }

    public int getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }

    public String[] getKids() {
        return kids;
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