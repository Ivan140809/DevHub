package com.skillstack.devhub.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;



@Document(collection = "usuarios")
public class User {

    @Id
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    private String apellido;

    @NotNull(message = "El username es obligatorio")
    @Size(min = 4, max = 15, message = "El nombre debe tener entre 4 y 15 caracteres")
    private String username;

    @Email(message = "El correo no es válido, asegurese de tener la estructura correo@ejemplo.com")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    private String contrasena;

    private List<String> preferencias;

    private Role rol;

    public User(){}

    public User(String nombre, String apellido, String username, String email,
                String contrasena, Role rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getId(){
        return id;
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

    public void setContrasena(String contrasena) { this.contrasena = contrasena;}

    public Role getRol() {
        return rol;
    }

    public List<String> getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(List<String> preferencias) {
        this.preferencias = preferencias;
    }
}