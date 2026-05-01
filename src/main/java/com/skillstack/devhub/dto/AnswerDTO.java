package com.skillstack.devhub.dto;

public class AnswerDTO {

    private String questionId;
    private String selectedOption;

    public AnswerDTO() {}

    public AnswerDTO(String questionId, String selectedOption) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }


}
