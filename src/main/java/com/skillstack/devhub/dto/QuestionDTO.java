package com.skillstack.devhub.dto;

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

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotNull(message = "La dificultad es obligatoria")
    private Dificultad dificultad;

    @NotEmpty(message = "Debe haber al menos una opción")
    private List<OptionDTO> opciones;

    public QuestionDTO() {}

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
