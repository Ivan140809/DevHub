package com.skillstack.devhub.dto;

public class ProgressDTO {

    private int totalAnswered;
    private double percentage;

    public ProgressDTO() {}

    public static class Builder {
        private int totalAnswered;
        private double percentage;

        public Builder totalAnswered(int totalAnswered) {
            this.totalAnswered = totalAnswered;
            return this;
        }

        public Builder percentage(double percentage) {
            this.percentage = percentage;
            return this;
        }

        public ProgressDTO build() {
            ProgressDTO dto = new ProgressDTO();
            dto.totalAnswered = this.totalAnswered;
            dto.percentage = this.percentage;
            return dto;
        }
    }

    public int getTotalAnswered(){
        return totalAnswered;
    }

    public double getPercentage(){
        return percentage;
    }
}
