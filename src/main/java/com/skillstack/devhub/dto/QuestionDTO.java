package com.skillstack.devhub.dto;

import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Dificultad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class QuestionDTO {

    @NotBlank(message = "La pregunta debe llevar un titulo")
    private String titulo;

    @NotBlank(message = "El enunciado es obligatorio")
    private String enunciado;

    @NotNull(message = "La categoría es obligatoria")
    private Category category;

    @NotNull(message = "La dificultad es obligatoria")
    private Dificultad dificultad;

    @NotEmpty(message = "Debe haber al menos una opción")
    private List<OptionDTO> opciones;

    public QuestionDTO(String titulo, String enunciado, Category category, Dificultad dificultad, List<OptionDTO> opciones) {
        this.titulo = titulo;
        this.enunciado = enunciado;
        this.category = category;
        this.dificultad = dificultad;
        this.opciones = opciones;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Category getCategoria() {
        return category;
    }

    public void setCategoria(Category category) {
        this.category = category;
    }

    public Dificultad getDificultad() {
        return dificultad;
    }

    public void setDificultad(Dificultad dificultad) {
        this.dificultad = dificultad;
    }

    public List<OptionDTO> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OptionDTO> opciones) {
        this.opciones = opciones;
    }

}
