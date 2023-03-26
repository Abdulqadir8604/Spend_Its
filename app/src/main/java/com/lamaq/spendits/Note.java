package com.lamaq.spendits;

import io.realm.RealmObject;

public class Note extends RealmObject {
    String title;
    String description;
    long createdTime;

    private Collection collection = null;

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Collection getCollection() {
        return collection;
    }

    public String getAmt() {
        return title;
    }

    public void setAmt(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
