package com.skillstack.devhub.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class AdminUser extends User {
    public AdminUser(String firstName, String lastName, String username, String email, String password, String phone, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }
}
