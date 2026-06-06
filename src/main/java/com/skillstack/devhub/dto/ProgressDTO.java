package com.skillstack.devhub.dto;

public class ProgressDTO {

    private final int totalAnswered;
    private final double percentage;

    public ProgressDTO(int totalAnswered, double percentage) {
        this.totalAnswered = totalAnswered;
        this.percentage = percentage;
    }

    public int getTotalAnswered() {
        return totalAnswered;
    }

    public double getPercentage() {
        return percentage;
    }

}
