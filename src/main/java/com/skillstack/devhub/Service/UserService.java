package com.skillstack.devhub.Service;

import com.skillstack.devhub.Model.User;
import com.skillstack.devhub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//aqui se manejara la logica de negocio, se llama a los repositorios 
//y se hacen las operaciones necesarias para cumplir con los 
//requerimientos de la aplicacion
public class UserService {

    //crear usuarios, listarlos, actualizarlos, eliminarlos, etc
    //metodos para manejar la logica de negocio relacionada con los usuarios
    //para esto se hace uso de los metodos del repositorio, como save, findAll, findById, deleteById, etc

    @Autowired
    private UserRepository userRepository;

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

        if (!password.matches(".*[0-9].*")) {
            return "Debe contener al menos un número";
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            return "Debe contener al menos un símbolo (@#$%^&+=!)";
        }

        return "OK";
    }

    public User register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("El username ya está en uso");
        }
        String respuesta=validarContrasena(user.getContraseña());

        if(!respuesta.equals("OK")){
            throw new RuntimeException(respuesta);
        }

        return userRepository.save(user);
    }

}