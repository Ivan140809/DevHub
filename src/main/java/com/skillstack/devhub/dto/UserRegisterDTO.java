package com.skillstack.devhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotNull(message = "El username es obligatorio")
    @Size(min = 4, max = 15, message = "El username debe tener entre 4 y 15 caracteres")
    private String username;

    @Email(message = "El correo no es válido, asegurese de tener la estructura correo@ejemplo.com")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 8, message = "La contrasena debe contener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "NUMERO DE TELEFONO OBLIGATORIO")
    private String phone;

    public UserRegisterDTO() {}

    public UserRegisterDTO(String firstName, String lastName, String username, String email, String password, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }
}
