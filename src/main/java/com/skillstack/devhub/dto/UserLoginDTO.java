package com.skillstack.devhub.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UserLoginDTO {

    @Email(message = "El correo no es válido, asegurese de tener la estructura correo@ejemplo.com")
    @NotBlank(message = "El correo es obligatorio")
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    private String password;

    public UserLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
