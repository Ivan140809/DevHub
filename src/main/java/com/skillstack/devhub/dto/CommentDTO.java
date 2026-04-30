package com.skillstack.devhub.dto;

import java.util.List;

public class CommentDTO {

    private String id;
    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private String username;
    private boolean isStarred;
    private List<CommentDTO> replies;
    private int happyFace;
    private int sadFace;

    public CommentDTO(String id, String title, String content, String category, List<String> tags, String username, boolean isStarred, List<CommentDTO> replies, int happyFace, int sadFace) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.username = username;
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

    public void setHappyFace(int happyFace) {
        this.happyFace = happyFace;
    }

    public int getSadFace() {
        return sadFace;
    }

    public void setSadFace(int sadFace) {
        this.sadFace = sadFace;
    }
}
