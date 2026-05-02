
package com.skillstack.devhub.service;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.dto.*;
import com.skillstack.devhub.exception.IncorrectPasswordException;
import com.skillstack.devhub.exception.UserAlreadyExistsException;
import com.skillstack.devhub.factorymethod.DefaultUserFactory;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.AnswerRepository;
import com.skillstack.devhub.repository.UserRepository;
import com.skillstack.devhub.security.JwtUtil;
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
    @CasoPrueba(
            id = "CP03",
            descripcion = "updateUser",
            entrada = "Email = test@test.com\n" +
                    "FirstName = NewPepe\n" +
                    "LastName = ElMalo\n" +
                    "Phone = 123456\n",
            tipo = "Normal",
            esperado = "Se retorna un User Response TO con los datos actualizados"
    )
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
    @CasoPrueba(
            id = "CP10",
            descripcion = "updateUser (Validar que el nuevo Username que ya tenía no sea el mismo al nuevo)\n",
            entrada =  "OldUserName=PepeElMalo\n" +
                    "NewUserName=PepeElMalo\n",
            tipo = "Logica Negocio",
            esperado = "Debe validar que no sea el mismo username y no dejarselo actualizar"
    )
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
    @CasoPrueba(
            id = "CP04",
            descripcion = "getProfile",
            entrada =  "Email = test@test.com",
            tipo = "Normal",
            esperado = "Se retorna un User Response TO con toda la información del usuario"
    )
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
    @CasoPrueba(
            id = "CP05",
            descripcion = "findRanking",
            entrada =  "User1:\n" +
                    "Username=user1\n" +
                    "Email=u1@test.com\n" +
                    "TotalScore=200\n" +
                    "User2:\n" +
                    "Username=user2\n" +
                    "Email=u2@test.com\n" +
                    "TotalScore=100\n",
            tipo = "Normal",
            esperado = "Se retorna una lista de usuarios en el ranking"
    )
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
    @CasoPrueba(
            id = "CP09",
            descripcion = "updateUser\n" +
                    "(Longitud máxima permitida del username = 15)\n",
            entrada =  "userName=abcdefghijklmno",
            tipo = "Borde",
            esperado = "Usuario actualizado correctamente"
    )
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
    @CasoPrueba(
            id = "CP01",
            descripcion = "Register",
            entrada =  "Email = test@pepe.com\n" +
                    "Username = testPepe\n" +
                    "Password = Pep3Malo!\n" +
                    "FirstName = Pepe\n" +
                    "LastName = ElMalo\n" +
                    "Phone = 3125660427\n",
            tipo = "Normal",
            esperado = "USUARIO REGISTRADO EXITOSAMENTE"
    )
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
    @CasoPrueba(
            id = "CP02",
            descripcion = "Login",
            entrada =  "Email = test@pepe.com\n" +
                    "Password = Pep3Malo!\n",
            tipo = "Normal",
            esperado = "Token Generado Igual al esperado"
    )
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
    @CasoPrueba(
            id = "CP07",
            descripcion = "Login\n" +
                    "(Contraseña incorrecta)\n",
            entrada =  "Email = test@pepe.com\n" +
                    "Password = \n" +
                    "Pep3Bueno!\n",
            tipo = "Negativa",
            esperado = "IncorrectPasswordException"
    )
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
    @CasoPrueba(
            id = "CP08",
            descripcion = "Register\n" +
                    "(Longitud mínima de contraseña permitida)\n",
            entrada =  "Email = test@pepe.com\n" +
                    "Username=\n" +
                    "pepe\n" +
                    "Password = Pepe1!\n",
            tipo = "Borde",
            esperado = "Usuario Registrado Correctamente"
    )
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
    @CasoPrueba(
            id = "CP06",
            descripcion = "Register\n" +
                    "(Email ya registrado)\n",
            entrada =  "Email = test@pepe.com\n",
            tipo = "Negativa",
            esperado = "UserAlreadyExistsException"
    )
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

