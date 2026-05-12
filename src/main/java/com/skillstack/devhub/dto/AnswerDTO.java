package com.skillstack.devhub.dto;

import java.time.Duration;

public class AnswerDTO {

    private String questionId;
    private String selectedOption;
    private Duration timerDTO;

    public AnswerDTO() {}

    public AnswerDTO(String questionId, String selectedOption) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public void setTimerDTO(Duration timerDTO) {
        this.timerDTO = timerDTO;
    }

    public Duration getTimerDTO() {
        return timerDTO;
    }
}
