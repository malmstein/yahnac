package com.malmstein.hnews.model;

public class Item {

    private final String by;
    private final int id;
    private final String type;
    private final Long time;

    public Item(String by, int id, String type, Long time) {
        this.by = by;
        this.id = id;
        this.type = type;
        this.time = time;
    }

    public String getBy() {
        return by;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Long getTime() {
        return time;
    }
}
