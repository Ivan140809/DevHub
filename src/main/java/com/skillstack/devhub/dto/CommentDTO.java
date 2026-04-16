package com.skillstack.devhub.dto;

import java.util.List;

public class CommentDTO {

    private String id;
    private String content;
    private String username;
    private List<CommentDTO> replies;

    public CommentDTO(String id, String content, String username, List<CommentDTO> replies) {
        this.id = id;
        this.content = content;
        this.username = username;
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
}
