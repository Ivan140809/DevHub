package com.skillstack.devhub.dto;

import com.skillstack.devhub.model.Role;

public class LoginResponseDTO {

    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String phone;

    public LoginResponseDTO(String token, String email, String firstName, String lastName, String username, String phone) {
        this.token = token;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.phone = phone;
    }

    public String getToken()     { return token; }
    public String getEmail()     { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getUsername()  { return username; }
    public String getPhone()     { return phone; }
}
