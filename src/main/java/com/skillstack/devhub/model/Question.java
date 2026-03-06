package com.skillstack.devhub.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "preguntas")
public class Question {

    @Id
    private String id;

    private String title;

    @NotBlank(message = "El enunciado no puede estar vacío")
    private String enunciado;

    @NotBlank(message = "La categoría no puede estar vacía")
    private String categoria;

    @NotNull(message = "La dificultad es obligatoria")
    private Dificultad dificultad;

    @NotEmpty(message = "Debe existir al menos una opción")
    private List<Option> opciones = new ArrayList<>();

    public Question() {
    }

    public Question(String title, String enunciado, String categoria, Dificultad dificultad, List<Option> opciones) {
        this.enunciado = enunciado;
        this.categoria = categoria;
        this.dificultad = dificultad;
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

    public List<Option> getOpciones() {
        return opciones;
    }

    public void setOpciones(ArrayList<Option> opciones) {
        this.opciones = opciones;
    }

}
