package com.skillstack.devhub.controller;


import com.skillstack.devhub.dto.ForgotPasswordDTO;
import com.skillstack.devhub.dto.LoginResponseDTO;
import com.skillstack.devhub.dto.PromoteAdminDTO;
import com.skillstack.devhub.dto.ResetPasswordDTO;
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
        String response = authenticationService.register(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserLoginDTO request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.login(request));
    }

    @PostMapping("/promote-admin")
    public ResponseEntity<String> promoteToAdmin(@RequestBody PromoteAdminDTO request) {
        String response = authenticationService.promoteToAdmin(request.getEmail(), request.getAdminKey());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO request) {
        String response = authenticationService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO request) {
        String response = authenticationService.resetPasswordWithCode(
                request.getEmail(), request.getCode(), request.getNewPassword());
        return ResponseEntity.ok(response);
    }
}
