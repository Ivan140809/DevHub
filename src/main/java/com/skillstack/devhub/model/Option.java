package com.skillstack.devhub.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Option {

    private String text;
    private Boolean correct;

    public Option() {
    }

    public Option(String text, Boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    // getters and setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
