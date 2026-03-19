package com.skillstack.devhub.dto;

import com.skillstack.devhub.model.Role;

public class LoginResponseDTO {

    private String token;
    private String email;

    public LoginResponseDTO(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
