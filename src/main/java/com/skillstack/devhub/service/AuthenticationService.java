package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.LoginResponseDTO;
import com.skillstack.devhub.dto.UserLoginDTO;
import com.skillstack.devhub.dto.UserRegisterDTO;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
import com.skillstack.devhub.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String validarContrasena(String password){

        if (!password.matches(".*[A-Z].*")) {
            return "Debe contener al menos una mayúscula";
        }

        if (!password.matches(".*[a-z].*")) {
            return "Debe contener al menos una minúscula";
        }

        if (!password.matches(".*\\d.*")) {
            return "Debe contener al menos un número";
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            return "Debe contener al menos un símbolo (@#$%^&+=!)";
        }

        return "OK";
    }

    public void register(UserRegisterDTO user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El email ya está en uso"
            );
        }

        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El username ya está en uso"
            );
        }
        String respuesta=validarContrasena(user.getContrasena());
        if(!respuesta.equals("OK")){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    respuesta
            );
        }
        User u = new User(user.getNombre(), user.getApellido(), user.getUsername(), user.getEmail(),
                user.getContrasena(), Role.USER);

        u.setContrasena(passwordEncoder.encode(user.getContrasena()));
        u.setPhone(user.getPhone());

        userRepository.save(u);
    }

    public LoginResponseDTO login(UserLoginDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDTO(token, user.getEmail(), user.getNombre(), user.getApellido(), user.getUsername(), user.getPhone());
    }

}
