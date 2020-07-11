package com.example.memotica;

public class ListItem {
    private long id = 0;
    private String title = null;
    private String content = null;
    private String updated = null;

    long getId() { return id; }
    String getTitle() { return title; }
    String getContent() { return content; }
    String getUpdated() { return updated; }

    void setId(long id) { this.id = id; }
    void setTitle(String title) { this.title = title; }
    void setContent(String content) { this.content = content; }
    void setUpdated(String updated) { this.updated = updated; }
}
