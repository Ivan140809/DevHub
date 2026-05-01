package com.skillstack.devhub.dto;

public class CreateReplyRequest {

    private String content;

    public CreateReplyRequest() {
    }

    public CreateReplyRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
