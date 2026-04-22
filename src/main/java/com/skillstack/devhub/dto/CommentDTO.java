package com.skillstack.devhub.dto;

import java.util.List;

public class CommentDTO {

    private String id;
    private String content;
    private String username;
    private boolean isStarred;
    private List<CommentDTO> replies;

    public CommentDTO(String id, String content, String username, boolean isStarred, List<CommentDTO> replies) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.isStarred = isStarred;
        this.replies = replies;
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

}
