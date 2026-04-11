package com.skillstack.devhub.controller;
import com.skillstack.devhub.dto.UserResponseDTO;
import com.skillstack.devhub.dto.UserUpdateDTO;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
import com.skillstack.devhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        UserResponseDTO userDTO = userService.getProfileUpdate(user);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String email, @RequestBody UserUpdateDTO updatedUser) {
        UserResponseDTO user = userService.updateUser(email, updatedUser);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<String>> getRanking() {
        List<String> ranking = userService.findRanking();
        return ResponseEntity.ok(ranking);
    }
}
