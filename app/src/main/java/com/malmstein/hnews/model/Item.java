package com.malmstein.hnews.model;

public class Item {

    public enum TYPE{
        story,
        comment,
        poll,
        pollopt,
        show,
        ask
    }

    private final int internalId;
    private final String by;
    private final int id;
    private final String type;
    private final Long time;

    public Item(int internalId, String by, int id, String type, Long time) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.type = type;
        this.time = time;
    }

    public int getInternalId() {
        return internalId;
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
