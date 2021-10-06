package com.example.techmaster.model;

public class Reply {
    String description;
    String date;
    String uid;
    String status;
    String url;
    String key;
    String postKey;

    public Reply() {
    }

    public Reply(String postKey, String key,String description, String date, String uid, String status, String url) {
        this.description = description;
        this.date = date;
        this.uid = uid;
        this.status = status;
        this.url = url;
        this.key = key;
        this.postKey = postKey;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }
    public String getDate() {
        return date;
    }

    public String getUid() {
        return uid;
    }

    public String getStatus() {
        return status;
    }

    public String getUrl() {
        return url;
    }
}
