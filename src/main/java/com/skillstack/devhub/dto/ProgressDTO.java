package com.skillstack.devhub.dto;

public class ProgressDTO {

    private int totalAnswered;
    private double percentage;

    public ProgressDTO(int totalAnswered, double percentage) {
        this.totalAnswered = totalAnswered;
        this.percentage = percentage;
    }

}
