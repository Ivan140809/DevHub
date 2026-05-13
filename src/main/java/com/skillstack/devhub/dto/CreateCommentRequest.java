package com.skillstack.devhub.dto;

import java.util.List;

public class CreateCommentRequest {

    private String title;
    private String content;
    private String category;
    private List<String> tags;

    public CreateCommentRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

}