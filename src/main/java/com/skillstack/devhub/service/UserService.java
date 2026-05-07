package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.RankingDTO;
import com.skillstack.devhub.dto.UserResponseDTO;
import com.skillstack.devhub.dto.UserUpdateDTO;
import com.skillstack.devhub.exception.UserAlreadyExistsException;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.AbstractUser;
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

    public void deleteAccount(String userId){
        AbstractUser user = userRepository.findById(userId)
                .orElseThrow( () -> new UserNotFoundException("USER NO ENCONTRADO"));

        userRepository.delete(user);

    }

    public List<RankingDTO> findRanking(){

        List<User> users = userRepository.findTop50ByOrderByTotalScoreDesc();

        if(users.isEmpty()){
            throw new UserNotFoundException("NO HAY USUARIOS ACTUALMENTE EN EL RANKING");
        }

        return users.stream()
                .map((user) -> {
                    int position = users.indexOf(user) + 1;
                    return new RankingDTO(position, user.getUsername(), user.getEmail(), user.getTotalScore());
                })
                .toList();

    }

    public UserResponseDTO getProfile(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("USER NO ENCONTRADO"));

        int answeredQuestions = answerRepository.findDistinctQuestionIdByUserId(user.getId()).size();
        System.out.println("GET PROFILE USER EMAIL: " + user.getEmail());
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getPreferences(),
                answeredQuestions,
                user.getTotalScore()
        );
    }

    public UserResponseDTO updateUser(String userEmail, UserUpdateDTO userUpdateDTO) {
        System.out.println("BUSCANDO USUARIO POR EMAIL");
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("USER NO ENCONTRADO"));

        if (userUpdateDTO.getFirstName() != null && !userUpdateDTO.getFirstName().isEmpty()) {
            user.setFirstName(userUpdateDTO.getFirstName());

        }

        if (userUpdateDTO.getLastName() != null && !userUpdateDTO.getLastName().isEmpty()) {
            user.setLastName(userUpdateDTO.getLastName());
        }

        if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isEmpty()
                && !userUpdateDTO.getUsername().equals(user.getUsername())) {

            String username = userUpdateDTO.getUsername();

            if (username.length() < 4 || username.length() > 15) {
                throw new IllegalArgumentException("El username debe tener entre 4 y 15 caracteres");
            }

            if (userRepository.existsByUsername(username)) {
                throw new UserAlreadyExistsException("USERNAME YA EN USO");
            }

            user.setUsername(username);
        }

        if (userUpdateDTO.getPhone() != null && !userUpdateDTO.getPhone().isEmpty()) {
            user.setPhone(userUpdateDTO.getPhone());
        }

        if (userUpdateDTO.getPreferences() != null && !userUpdateDTO.getPreferences().isEmpty()) {
            user.setPreferences(userUpdateDTO.getPreferences());
        }

        userRepository.save(user);

        return getProfile(user.getEmail());
    }
}
