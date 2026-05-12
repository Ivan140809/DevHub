package com.skillstack.devhub.model;

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

}
