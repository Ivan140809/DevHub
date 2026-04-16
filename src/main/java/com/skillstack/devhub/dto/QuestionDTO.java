package com.skillstack.devhub.dto;

import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class QuestionDTO {

    private String id;

    @NotBlank(message = "La pregunta debe llevar un titulo")
    private String title;

    @NotBlank(message = "El enunciado es obligatorio")
    private String statement;

    @NotNull(message = "La categoria es obligatoria")
    private Category category;

    @NotNull(message = "La dificultad es obligatoria")
    private Difficulty difficulty;

    @NotEmpty(message = "Debe haber al menos una opcion")
    private List<OptionDTO> options;

    public QuestionDTO(String id, String title, String statement, Category category, Difficulty difficulty, List<OptionDTO> options) {
        this.id=id;
        this.title = title;
        this.statement = statement;
        this.category = category;
        this.difficulty = difficulty;
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
