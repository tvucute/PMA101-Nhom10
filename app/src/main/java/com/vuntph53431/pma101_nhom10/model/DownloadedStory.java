package com.vuntph53431.pma101_nhom10.model;

public class DownloadedStory {
    private int storyId;
    private String title;
    private String content;

    public DownloadedStory(int storyId, String title, String content) {
        this.storyId = storyId;
        this.title = title;
        this.content = content;
    }

    public int getStoryId() { return storyId; }
    public void setStoryId(int storyId) { this.storyId = storyId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
        return title;
    }
}
