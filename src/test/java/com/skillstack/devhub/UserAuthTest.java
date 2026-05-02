
package com.skillstack.devhub;

import com.skillstack.devhub.dto.*;
import com.skillstack.devhub.exception.IncorrectPasswordException;
import com.skillstack.devhub.exception.UserAlreadyExistsException;
import com.skillstack.devhub.factorymethod.DefaultUserFactory;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.AnswerRepository;
import com.skillstack.devhub.repository.UserRepository;
import com.skillstack.devhub.security.JwtUtil;
import com.skillstack.devhub.service.AuthenticationService;
import com.skillstack.devhub.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAuthTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private DefaultUserFactory defaultUserFactory;
    @Mock
    private AnswerRepository answerRepository;
    @InjectMocks
    private AuthenticationService service;
    @InjectMocks
    private UserService userService;

    @Test
    void updateUserSuccessfully() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("OldPepe");
        user.setLastName("ElBueno");

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setFirstName("NewPepe");
        dto.setLastName("ElMalo");
        dto.setPhone("123456");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(answerRepository.findDistinctQuestionIdByUserId(any()))
                .thenReturn(List.of("q1", "q2"));

        // Act
        UserResponseDTO result = userService.updateUser("test@test.com", dto);

        // Assert
        assertNotNull(result);
        assertEquals("NewPepe", result.getFirstName());
        assertEquals("ElMalo", result.getLastName());
        assertEquals("123456", result.getPhone());

        verify(userRepository).save(user);
    }

    @Test
    void updateUserSameUserName() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setUsername("PepeElMalo");

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUsername("PepeElMalo");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(answerRepository.findDistinctQuestionIdByUserId(any()))
                .thenReturn(List.of("q1", "q2"));

        // Act
        UserResponseDTO result = userService.updateUser("test@test.com", dto);

        // Assert
        assertNotNull(result);
        assertEquals("PepeElMalo", result.getUsername());

        verify(userRepository, never()).existsByUsername(any());

        verify(userRepository).save(user);
    }

    @Test
    void getProfileSuccessfully() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("Pepe");
        user.setLastName("Perez");
        user.setPhone("123");
        user.setTotalScore(100);

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(answerRepository.findDistinctQuestionIdByUserId(any()))
                .thenReturn(List.of("q1", "q2", "q3"));

        // Act
        UserResponseDTO result = userService.getProfile("test@test.com");

        // Assert
        assertNotNull(result);
        assertEquals("Pepe", result.getFirstName());
        assertEquals("Perez", result.getLastName());
        assertEquals(3, result.getAnsweredQuestions());
        assertEquals(100, result.getTotalScore());

        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void findRankingSuccessfully() {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("u1@test.com");
        user1.setTotalScore(200);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("u2@test.com");
        user2.setTotalScore(100);

        when(userRepository.findTop50ByOrderByTotalScoreDesc())
                .thenReturn(List.of(user1, user2));

        // Act
        List<RankingDTO> ranking = userService.findRanking();

        // Assert
        assertNotNull(ranking);
        assertEquals(2, ranking.size());

        assertEquals(1, ranking.get(0).getPosition());
        assertEquals("user1", ranking.get(0).getUsername());

        assertEquals(2, ranking.get(1).getPosition());
        assertEquals("user2", ranking.get(1).getUsername());

        verify(userRepository).findTop50ByOrderByTotalScoreDesc();
    }

    @Test
    void updateUserUsernameMaxLength() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setUsername("oldUser");

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUsername("abcdefghijklmno");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(userRepository.existsByUsername(dto.getUsername()))
                .thenReturn(false);

        when(answerRepository.findDistinctQuestionIdByUserId(any()))
                .thenReturn(List.of());

        // Act
        UserResponseDTO result = userService.updateUser("test@test.com", dto);

        // Assert
        assertNotNull(result);
        assertEquals("abcdefghijklmno", result.getUsername());

        verify(userRepository).save(user);
    }

    @Test
    void RegisterUserSuccessfully() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setEmail("test@pepe.com");
        dto.setUsername("testPepe");
        dto.setPassword("Pep3Malo!");
        dto.setFirstName("Pepe");
        dto.setLastName("ElMalo");
        dto.setPhone("3125660427");

        User fakeUser = new User();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
        when(defaultUserFactory.createUser(any(), any(), any(), any(), any(), any(), eq(Role.USER)
                )).thenReturn(fakeUser);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPass");
        when(userRepository.count()).thenReturn(1L);

        String result = service.register(dto);

        assertEquals("USUARIO REGISTRADO CORRECTAMENTE", result);
        verify(userRepository).save(fakeUser);
    }

    @Test
    void LoginSuccessfully() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@pepe.com");
        dto.setPassword("Pep3Malo!");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword("encodedPass");

        when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail()))
                .thenReturn("token123");

        LoginResponseDTO response = service.login(dto);

        assertEquals("token123", response.getToken());
    }
    @Test
    void IncorrectPassword() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@pepe.com");
        dto.setPassword("Pep3Bueno!");

        User user = new User();
        user.setPassword("encodedPass");

        when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> {
            service.login(dto);
        });
    }

    @Test
    void BorderShortPassword() {

        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setEmail("test@pepe.com");
        userDTO.setUsername("pepe");
        userDTO.setPassword("Pepe1!");

        User fakeUser = new User();

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(defaultUserFactory.createUser(
                any(), any(), any(), any(), any(), any(), eq(Role.USER)
        )).thenReturn(fakeUser);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.count()).thenReturn(1L);

        String result = service.register(userDTO);

        assertEquals("USUARIO REGISTRADO CORRECTAMENTE", result);
        verify(userRepository).save(fakeUser);
    }

    @Test
    void EmailAlreadyExists() {
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setEmail("test@pepe.com");

        when(userRepository.findByEmail(userDTO.getEmail()))
                .thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> {
            service.register(userDTO);
        });

        verify(userRepository, never()).save(any());
    }

}

