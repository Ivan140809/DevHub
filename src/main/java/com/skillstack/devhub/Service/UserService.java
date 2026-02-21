package com.skillstack.devhub.Service;

import com.skillstack.devhub.Model.User;
import com.skillstack.devhub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public User register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("El username ya está en uso");
        }

        return userRepository.save(user);
    }

}