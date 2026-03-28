package com.skillstack.devhub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="answers")
public class Answer {
    @Id
    private String questionId;
    private String selectedOption;

    public Answer(String questionId, String selectedOption) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }
}
