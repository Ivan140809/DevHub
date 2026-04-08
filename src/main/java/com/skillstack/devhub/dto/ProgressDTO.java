package com.skillstack.devhub.dto;

public class ProgressDTO {

    private int totalAnswered;
    private double percentage;

    public ProgressDTO(int totalAnswered, double percentage) {
        this.totalAnswered = totalAnswered;
        this.percentage = percentage;
    }

    public int getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(int totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
