package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.UserRegisterDTO;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
//aqui se manejara la logica de negocio, se llama a los repositorios 
//y se hacen las operaciones necesarias para cumplir con los 
//requerimientos de la aplicacion
public class UserService {

    //crear usuarios, listarlos, actualizarlos, eliminarlos, etc
    //metodos para manejar la logica de negocio relacionada con los usuarios
    //para esto se hace uso de los metodos del repositorio, como save, findAll, findById, deleteById, etc

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
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

        User u = new User(user.getNombre(), user.getApellido(), user.getUsername(), user.getEmail(),
                user.getContrasena(), user.getPreferencias());

        if (userRepository.findByEmail(u.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El email ya está en uso"
            );
        }

        if(userRepository.findByUsername(u.getUsername()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El username ya está en uso"
            );
        }
        String respuesta=validarContrasena(u.getContrasena());


        if(!respuesta.equals("OK")){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    respuesta
            );
        }

        userRepository.save(u);
    }

}