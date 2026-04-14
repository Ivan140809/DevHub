package com.skillstack.devhub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public class User extends AbstractUser{
    @Id
    private String id;

    public User(String firstName, String lastName, String username, String email, String password, String phone, Role role, int totalScore) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.phone = phone;
        this.totalScore = totalScore;
    }

    public String getId() {
        return id;
    }

}
