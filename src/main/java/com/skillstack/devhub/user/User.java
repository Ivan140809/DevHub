package com.skillstack.devhub.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "usuarios")
public class User {

    @Id
    private String id;

    private String nombre;
    private String apellido;
    private String username;
    private String email;
    private String contraseña;
    private ArrayList<String> preferencias;
}
