package com.skillstack.devhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OpcionDTO {

    @NotBlank(message = "El texto de la opción es obligatorio")
    private String texto;

    @NotNull(message = "Debe indicarse si la opción es correcta")
    private Boolean esCorrecta;

    public OpcionDTO() {
    }

    public OpcionDTO(String texto, Boolean esCorrecta) {
        this.texto = texto;
        this.esCorrecta = esCorrecta;
    }

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
