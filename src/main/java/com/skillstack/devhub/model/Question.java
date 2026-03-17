package com.skillstack.devhub.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Document(collection = "preguntas")
public class Question {

    @Id
    private String id;

    private String title;

    @NotBlank(message = "El enunciado no puede estar vacío")
    private String enunciado;

    @NotBlank(message = "La categoría no puede estar vacía")
    private Category category;

    @NotNull(message = "La dificultad es obligatoria")
    private Difficulty difficulty;

    @NotEmpty(message = "Debe existir al menos una opción")
    private List<Option> opciones;

    public Question() {
    }

    public Question(String title, String enunciado, Category category, Difficulty difficulty, List<Option> opciones) {
        this.title = title;
        this.enunciado = enunciado;
        this.category = category;
        this.difficulty = difficulty;
        this.opciones = opciones;
    }

    public void agregarOpcion(Option opcion) {
        this.opciones.add(opcion);
    }

    //getters yyy setters


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Difficulty getDificultad() {
        return difficulty;
    }

    public void setDificultad(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Option> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<Option> opciones) {
        this.opciones = opciones;
    }

}
