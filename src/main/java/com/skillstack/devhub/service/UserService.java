package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.RankingDTO;
import com.skillstack.devhub.dto.UserResponseDTO;
import com.skillstack.devhub.dto.UserUpdateDTO;
import com.skillstack.devhub.exception.UserAlreadyExistsException;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.AnswerRepository;
import com.skillstack.devhub.repository.QuestionRepository;
import com.skillstack.devhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Service
public class UserService {

    private static final String USER_NOT_FOUND = "USER NO ENCONTRADO";

    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public UserService(UserRepository userRepository, AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    private User findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    private void applyIfPresent(String value, Consumer<String> setter) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }

    public void promoteToAdmin(String email) {
        User user = findUserByEmailOrThrow(email);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    public void deleteAccount(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        userRepository.delete(user);
    }

    public List<RankingDTO> findRanking() {
        List<User> users = userRepository.findTop50ByOrderByTotalScoreDesc();
        if (users.isEmpty()) {
            throw new UserNotFoundException("NO HAY USUARIOS ACTUALMENTE EN EL RANKING");
        }
        return IntStream.range(0, users.size())
                .mapToObj(i -> new RankingDTO(i + 1, users.get(i).getUsername(),
                        users.get(i).getEmail(), users.get(i).getTotalScore()))
                .toList();
    }

    public UserResponseDTO getProfile(String userEmail) {
        User user = findUserByEmailOrThrow(userEmail);
        int answeredQuestions = (int) answerRepository.countByUserId(user.getId());
        int totalQuestions = (int) questionRepository.count();
        String roleStr = user.getRole() != null ? user.getRole().name() : "USER";
        UserResponseDTO dto = new UserResponseDTO(
                user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getEmail(), user.getPhone(), user.getPreferences(),
                answeredQuestions, user.getTotalScore(), roleStr);
        dto.setTotalQuestions(totalQuestions);
        return dto;
    }

    public UserResponseDTO updateUser(String userEmail, UserUpdateDTO userUpdateDTO) {
        User user = findUserByEmailOrThrow(userEmail);

        applyIfPresent(userUpdateDTO.getFirstName(), user::setFirstName);
        applyIfPresent(userUpdateDTO.getLastName(), user::setLastName);
        applyIfPresent(userUpdateDTO.getPhone(), user::setPhone);
        if (userUpdateDTO.getPreferences() != null && !userUpdateDTO.getPreferences().isEmpty()) {
            user.setPreferences(userUpdateDTO.getPreferences());
        }

        String newUsername = userUpdateDTO.getUsername();
        if (newUsername != null && !newUsername.isEmpty() && !newUsername.equals(user.getUsername())) {
            if (newUsername.length() < 4 || newUsername.length() > 15) {
                throw new IllegalArgumentException("El username debe tener entre 4 y 15 caracteres");
            }
            if (userRepository.existsByUsername(newUsername)) {
                throw new UserAlreadyExistsException("USERNAME YA EN USO");
            }
            user.setUsername(newUsername);
        }

        userRepository.save(user);
        return getProfile(user.getEmail());
    }
}
