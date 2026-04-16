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

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
