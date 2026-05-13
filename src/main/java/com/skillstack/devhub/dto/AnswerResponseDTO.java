package com.skillstack.devhub.dto;

public class AnswerResponseDTO {

    private boolean correct;
    private int totalScore;

    public AnswerResponseDTO() {}

    public AnswerResponseDTO(boolean correct, int totalScore) {
        this.correct = correct;
        this.totalScore = totalScore;
    }

    public boolean isCorrect() {
        return correct;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
