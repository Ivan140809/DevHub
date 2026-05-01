
package com.skillstack.devhub;

import com.skillstack.devhub.dto.LoginResponseDTO;
import com.skillstack.devhub.dto.UserLoginDTO;
import com.skillstack.devhub.dto.UserRegisterDTO;
import com.skillstack.devhub.exception.IncorrectPasswordException;
import com.skillstack.devhub.exception.UserAlreadyExistsException;
import com.skillstack.devhub.factorymethod.DefaultUserFactory;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
import com.skillstack.devhub.security.JwtUtil;
import com.skillstack.devhub.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @InjectMocks
    private AuthenticationService service;

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

