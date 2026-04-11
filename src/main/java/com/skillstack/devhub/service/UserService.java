package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.UserResponseDTO;
import com.skillstack.devhub.dto.UserUpdateDTO;
import com.skillstack.devhub.exception.UserAlreadyExistsException;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.AnswerRepository;
import com.skillstack.devhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public UserService(UserRepository userRepository, AnswerRepository answerRepository) {
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    public List<String> findRanking(){

        List<User> users = userRepository.findTop50ByOrderByTotalScoreDesc();

        if(users.isEmpty()){
            throw new UserNotFoundException("NO HAY USUARIOS ACTUALMENTE EN EL RANKING");
        }
        return users.stream().map(User::getUsername).toList();

    }

    public UserResponseDTO getProfileUpdate(User user) {

        int answeredQuestions = answerRepository.findDistinctQuestionIdByUserId(user.getId()).size();

        int totalScore = user.getTotalScore();

        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getPreferences(),
                answeredQuestions,
                totalScore
        );
    }

    public UserResponseDTO updateUser(String email, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("USER NO ENCONTRADO"));

        if (userUpdateDTO.getFirstName() != null && !userUpdateDTO.getFirstName().isEmpty()) {
            user.setFirstName(userUpdateDTO.getFirstName());
        }

        if (userUpdateDTO.getLastName() != null && !userUpdateDTO.getLastName().isEmpty()) {
            user.setLastName(userUpdateDTO.getLastName());
        }

        if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isEmpty()
                && !userUpdateDTO.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(userUpdateDTO.getUsername())) {
                throw new UserAlreadyExistsException("USERNAME YA EN USO");
            }
            user.setUsername(userUpdateDTO.getUsername());
        }

        if (userUpdateDTO.getPhone() != null && !userUpdateDTO.getPhone().isEmpty()) {
            user.setPhone(userUpdateDTO.getPhone());
        }

        if (userUpdateDTO.getPreferences() != null && !userUpdateDTO.getPreferences().isEmpty()) {
            user.setPreferences(userUpdateDTO.getPreferences());
        }

        userRepository.save(user);

        return getProfileUpdate(user);
    }

}
