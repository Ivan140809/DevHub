package com.skillstack.devhub.dto;

import java.util.List;

public class UserRegisterDTO {
    private String nombre;
    private String apellido;
    private String username;
    private String email;
    private String contrasena;
    private String phone;


    public UserRegisterDTO(String nombre, String apellido, String username, String email, String contrasena,String phone) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.email = email;
        this.contrasena = contrasena;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getPhone() {
        return phone;
    }

}
