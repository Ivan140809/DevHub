package com.skillstack.devhub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="answers")
public class Answer {
    @Id
    private String questionId;
    private String selectedOption;
    private String userId;

    public Answer(String questionId, String selectedOption, String userId) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.userId = userId;
    }





    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
