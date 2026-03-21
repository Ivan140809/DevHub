package com.skillstack.devhub.dto;

import com.skillstack.devhub.model.Role;

public class LoginResponseDTO {

    private String token;
    private String email;
    private String nombre;
    private String apellido;
    private String username;
    private String phone;

    public LoginResponseDTO(String token, String email, String nombre, String apellido, String username, String phone) {
        this.token = token;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.phone = phone;
    }

    public String getToken()    { return token; }
    public String getEmail()    { return email; }
    public String getNombre()   { return nombre; }
    public String getApellido() { return apellido; }
    public String getUsername() { return username; }
    public String getPhone()    { return phone; }
}
