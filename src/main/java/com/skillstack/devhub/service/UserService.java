package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.UserLoginDto;
import com.skillstack.devhub.dto.UserRegisterDTO;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        userRepository.save(u);
    }


    public String login(UserLoginDto request){

        User u = userRepository.findByEmail(request.getEmail())
                .orElseThrow( ()->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Usuario o contraseña incorrectas"
                        ));

        if (!passwordEncoder.matches(request.getPassword(), u.getContrasena())) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Usuario o contraseña incorrectas"
            );
        }

        return "Login Exitoso";
    }

}
