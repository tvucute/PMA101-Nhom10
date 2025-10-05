package com.vuntph53431.pma101_nhom10.model;

public class Comment {
    private int id;
    private int storyId;
    private int rating;
    private String content;
    private String username;
    private String createdAt;

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // Getters + Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStoryId() { return storyId; }
    public void setStoryId(int storyId) { this.storyId = storyId; }

    public int getRating() { return rating; }
    public void setRating(float rating) { this.rating = (int) rating; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
