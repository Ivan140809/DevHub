package com.skillstack.devhub.dto;

import java.util.List;

public class CommentDTO {

    private final String id;
    private final String title;
    private final String content;
    private final String category;
    private final List<String> tags;
    private final String username;
    private final String createdAt;
    private final boolean isStarred;
    private final List<CommentDTO> replies;
    private final int happyFace;
    private final int sadFace;

    public CommentDTO(String id, String title, String content, String category, List<String> tags, String username, String createdAt, boolean isStarred, List<CommentDTO> replies, int happyFace, int sadFace) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.username = username;
        this.createdAt = createdAt;
        this.isStarred = isStarred;
        this.replies = replies;
        this.happyFace = happyFace;
        this.sadFace = sadFace;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUsername() {
        return username;
    }

    public List<CommentDTO> getReplies() {
        return replies;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public int getHappyFace() {
        return happyFace;
    }


    public int getSadFace() {
        return sadFace;
    }

}
