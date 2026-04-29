package com.skillstack.devhub.dto;

import java.util.List;

public class CommentDTO {

    private String id;
    private String content;
    private String username;
    private boolean isStarred;
    private List<CommentDTO> replies;
    private int happyFace;
    private int sadFace;

    public CommentDTO(String id, String content, String username, boolean isStarred, List<CommentDTO> replies, int happyFace, int sadFace) {
        this.id = id;
        this.content = content;
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
