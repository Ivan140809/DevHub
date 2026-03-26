package com.skillstack.devhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    @NotNull(message = "El username es obligatorio")
    @Size(min = 4, max = 15, message = "El nombre debe tener entre 4 y 15 caracteres")
    private String username;
    @Email(message = "El correo no es válido, asegurese de tener la estructura correo@ejemplo.com")
    @NotBlank(message = "El correo es obligatorio")
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    private String password;
    private String phone;


    public UserRegisterDTO(String nombre, String apellido, String username, String email, String password,String phone) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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
