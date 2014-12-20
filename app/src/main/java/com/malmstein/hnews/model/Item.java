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
    private final String type;
    private final Long time;
    private final Long updated;

    public Item(Long internalId, String by, Long id, String type, Long time, Long updated) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.type = type;
        this.time = time;
        this.updated = updated;
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

    public String getType() {
        return type;
    }

    public Long getTime() {
        return time;
    }

    public Long getUpdated() {
        return updated;
    }

}
