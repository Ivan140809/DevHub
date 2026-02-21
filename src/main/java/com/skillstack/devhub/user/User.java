package com.skillstack.devhub.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "usuarios")
public class User {

    @Id
    private String id;


    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    private String apellido;

    @NotNull(message = "El usenname es obligatorio")
    @Size(min = 4, max = 15, message = "El nombre debe tener entre 4 y 15 caracteres")
    private String username;

    @Email(message = "El correo no es válido, asegurese de tener la estructura correo@ejemplo.com")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @Size(min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    private String contraseña;
    private ArrayList<String> preferencias;
}
