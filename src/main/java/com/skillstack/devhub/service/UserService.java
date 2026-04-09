package com.skillstack.devhub.service;

import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public List<String> findRanking(){

        List<User> users = userRepository.findTop50ByOrderByTotalScoreDesc();

        if(users.isEmpty()){
            throw new UserNotFoundException("NO HAY USUARIOS ACTUALMENTE EN EL RANKING");
        }
        return users.stream().map(User::getUsername).toList();

    }

}
