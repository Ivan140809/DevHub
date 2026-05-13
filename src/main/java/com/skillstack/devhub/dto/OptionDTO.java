package com.skillstack.devhub.dto;

import com.skillstack.devhub.model.Option;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OptionDTO {

    @NotBlank(message = "El texto de la opcion es obligatorio")
    private String text;

    @NotNull(message = "Debe indicarse si la opcion es correcta")
    private Boolean correct;

    public OptionDTO() {
    }

    public OptionDTO(String text, Boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    public String getText() {
        return text;
    }

    public Boolean isCorrect() {
        return correct;
    }

    public Option toOption(){
        return new Option(text,correct);
    }

}
