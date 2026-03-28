package com.skillstack.devhub.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Option {

    private String texto;

    private Boolean esCorrecta;

    public Option() {
    }

    public Option(String texto, Boolean esCorrecta) {
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
