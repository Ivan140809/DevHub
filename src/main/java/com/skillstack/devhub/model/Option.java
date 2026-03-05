package com.skillstack.devhub.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Opcion {

    @NotBlank(message = "El texto de la opción no puede estar vacío")
    private String texto;

    @NotNull(message = "Debe indicarse si es correcta o no")
    private Boolean esCorrecta;

    public Opcion() {
    }

    public Opcion(String texto, Boolean esCorrecta) {
        this.texto = texto;
        this.esCorrecta = esCorrecta;
    }

    //getters yyy setters
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Boolean getEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(Boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }
}
