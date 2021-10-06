package com.example.techmaster.model;

public class Query {

   String office_holder;
    String office_num;
    String office_contact;
    String description;
    String issue_relation;
    String date;
    String uid;
    String key;
    String status;
    String attended;
    String url;

    public Query() {
    }

    public Query(String key, String url,String office_holder, String office_num, String office_contact, String description, String issue_relation, String date, String uid, String status, String attended) {
        this.office_holder = office_holder;
        this.office_num = office_num;
        this.office_contact = office_contact;
        this.description = description;
        this.issue_relation = issue_relation;
        this.date = date;
        this.uid = uid;
        this.status = status;
        this.attended = attended;
        this.key = key;
        this.url = url;
    }

    public String getKey() {
        return key;
    }
    public String getUrl() {
        return url;
    }
    public String getOffice_holder() {
        return office_holder;
    }

    public String getOffice_num() {
        return office_num;
    }

    public String getOffice_contact() {
        return office_contact;
    }

    public String getDescription() {
        return description;
    }

    public String getIssue_relation() {
        return issue_relation;
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

    public String getAttended() {
        return attended;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setOffice_holder(String office_holder) {
        this.office_holder = office_holder;
    }

    public void setOffice_num(String office_num) {
        this.office_num = office_num;
    }

    public void setOffice_contact(String office_contact) {
        this.office_contact = office_contact;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIssue_relation(String issue_relation) {
        this.issue_relation = issue_relation;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAttended(String attended) {
        this.attended = attended;
    }
}
