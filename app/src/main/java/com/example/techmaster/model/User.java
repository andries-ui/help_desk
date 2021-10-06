package com.example.techmaster.model;

public class User {

    public String email;
    public String contact;
    public String key;
    public String uri;
    public String id;

    public User(String email, String contact, String key, String uri, String id) {
        this.email = email;
        this.contact = contact;
        this.key = key;
        this.uri = uri;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getKey() {
        return key;
    }

    public String getUri() {
        return uri;
    }

    public String getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User() {
    }


}