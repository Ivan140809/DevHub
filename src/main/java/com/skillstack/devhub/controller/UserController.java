package com.skillstack.devhub.controller;

import com.skillstack.devhub.dto.RankingDTO;
import com.skillstack.devhub.dto.UserResponseDTO;
import com.skillstack.devhub.dto.UserUpdateDTO;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
import com.skillstack.devhub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
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
}
