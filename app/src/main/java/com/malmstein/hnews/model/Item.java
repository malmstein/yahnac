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

    private final Long internalId;
    private final String by;
    private final Long id;
    private final Long time;

    public Item(Long internalId, String by, Long id, Long time) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.time = time;
    }

    public Long getInternalId() {
        return internalId;
    }

    public String getBy() {
        return by;
    }

    public Long getId() {
        return id;
    }

    public Long getTime() {
        return time;
    }

}
