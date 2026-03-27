package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.LoginResponseDTO;
import com.skillstack.devhub.dto.UserLoginDTO;
import com.skillstack.devhub.dto.UserRegisterDTO;
import com.skillstack.devhub.exception.IncorrectPasswordException;
import com.skillstack.devhub.exception.PasswordFormatException;
import com.skillstack.devhub.exception.UserAlreadyExistsException;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
import com.skillstack.devhub.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            return " DEBE CONTENER AL MENOS UNA MAYÚSCULA";
        }

        if (!password.matches(".*[a-z].*")) {
            return " DEBE CONTENER AL MENOS UNA MINÚSCULA";
        }

        if (!password.matches(".*\\d.*")) {
            return " DEBE CONTENER AL MENOS UN NÚMERO";
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            return " DEBE CONTENER AL MENOS UN SÍMBOLO (@#$%^&+=!)";
        }

        return "OK";
    }

    public String register(UserRegisterDTO user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("EMAIL"+user.getEmail()+" YA EN USO");
        }

        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("USERNAME "+user.getUsername()+" YA EN USO");
        }

        String respuesta=validarContrasena(user.getPassword());
        if(!respuesta.equals("OK")){
            throw new PasswordFormatException("CONTRASEÑA "+ respuesta);
        }

        User u = new User(user.getNombre(), user.getApellido(), user.getUsername(), user.getEmail(),
                user.getPassword(), Role.USER);
        u.setContrasena(passwordEncoder.encode(user.getPassword()));
        u.setPhone(user.getPhone());

        userRepository.save(u);

        return "USUARIO REGISTRADO CORRECTAMENTE";
    }

    public LoginResponseDTO login(UserLoginDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("USUARIO CON EMAIL "+request.getEmail()+" NO ENCONTRADO"));

        if (!passwordEncoder.matches(request.getPassword(), user.getContrasena())) {
            throw new IncorrectPasswordException("CONTRASEÑA INCORRECTA");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDTO(token, user.getEmail(), user.getNombre(), user.getApellido(), user.getUsername(), user.getPhone());
    }

}
