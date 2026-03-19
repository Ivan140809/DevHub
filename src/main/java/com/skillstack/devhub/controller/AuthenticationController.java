package com.skillstack.devhub.controller;


import com.skillstack.devhub.dto.LoginResponseDTO;
import com.skillstack.devhub.dto.UserLoginDTO;
import com.skillstack.devhub.dto.UserRegisterDTO;
import com.skillstack.devhub.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterDTO user) {
        authenticationService.register(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Usuario registrado correctamente");
       
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody UserLoginDTO request) {
        return authenticationService.login(request);
    }
}
