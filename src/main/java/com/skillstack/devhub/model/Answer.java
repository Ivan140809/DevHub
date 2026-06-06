package com.skillstack.devhub.model;

import java.time.Duration;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="answers")
public class Answer {
    @Id
    private String id;
    private final String questionId;
    private final String selectedOption;
    private String userId;
    private final Duration timer;

    public Answer(String questionId, String selectedOption, String userId, Duration timer) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.userId = userId;
        this.timer = timer;
    }

    public String getId() {
        return id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public Duration getTimer() {
        return timer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
