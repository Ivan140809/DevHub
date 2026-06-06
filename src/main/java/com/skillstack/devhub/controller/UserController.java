package com.skillstack.devhub.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstack.devhub.dto.RankingDTO;
import com.skillstack.devhub.dto.UserResponseDTO;
import com.skillstack.devhub.dto.UserUpdateDTO;
import com.skillstack.devhub.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //@PreAuthorize("hasRole('USER')")
    @PatchMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateUser(Principal principal, @Valid @RequestBody UserUpdateDTO updatedUser) {
        UserResponseDTO user = userService.updateUser(principal.getName(), updatedUser);
        return ResponseEntity.ok(user);
    }

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(Principal principal) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getProfile(principal.getName()));
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingDTO>> getRanking() {

        List<RankingDTO> ranking = userService.findRanking();

        return ResponseEntity.ok(ranking);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteAccount(Principal principal) {
        UserResponseDTO user = userService.getProfile(principal.getName());
        userService.deleteAccount(user.getId());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{email}/promote")
    public ResponseEntity<String> promoteUser(@PathVariable String email) {
        userService.promoteToAdmin(email);
        return ResponseEntity.ok("Usuario " + email + " promovido a ADMIN");
    }
}
