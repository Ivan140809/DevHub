package com.skillstack.devhub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;

@Document(collection="answers")
public class Answer {
    @Id
    private String id;
    private String questionId;
    private String selectedOption;
    private String userId;
    private Duration timer;

    public Answer(String questionId, String selectedOption, String userId, Duration timer) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.userId = userId;
        this.timer = timer;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
