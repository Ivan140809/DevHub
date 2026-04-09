package com.skillstack.devhub.model;

public class Progress {
    private int totalAnswered;
    private double percentage;

    public void setTotalAnswered(int totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getTotalAnswered() {
        return totalAnswered;
    }

    public double getPercentage() {
        return percentage;
    }
}
