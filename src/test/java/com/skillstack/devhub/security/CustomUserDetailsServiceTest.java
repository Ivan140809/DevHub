package com.skillstack.devhub.security;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @CasoPrueba(
            id = "CP72",
            descripcion = "CustomUserDetailsService - carga usuario por email correctamente",
            entrada = "email=test@test.com, role=USER",
            tipo = "Normal",
            esperado = "Retorna UserDetails con email y autoridad ROLE_USER"
    )
    void loadUserByUsername_foundByEmail() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPass");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        UserDetails details = customUserDetailsService.loadUserByUsername("test@test.com");

        assertNotNull(details);
        assertEquals("test@test.com", details.getUsername());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(userRepository).findByEmail("test@test.com");
        verify(userRepository, never()).findByUsername(any());

        System.out.println("CP72 Usuario cargado por email correctamente");
        System.out.println("Username: " + details.getUsername());
        System.out.println("Authorities: " + details.getAuthorities());
    }

    @Test
    @CasoPrueba(
            id = "CP73",
            descripcion = "CustomUserDetailsService - carga usuario por username cuando no existe por email",
            entrada = "username=testuser, email=test@test.com, role=ADMIN",
            tipo = "Normal",
            esperado = "Retorna UserDetails con email del usuario y autoridad ROLE_ADMIN"
    )
    void loadUserByUsername_foundByUsername() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setUsername("testuser");
        user.setPassword("encodedPass");
        user.setRole(Role.ADMIN);

        when(userRepository.findByEmail("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails details = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(details);
        assertEquals("test@test.com", details.getUsername());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        System.out.println("CP73 Usuario cargado por username correctamente");
        System.out.println("Username: " + details.getUsername());
        System.out.println("Authorities: " + details.getAuthorities());
    }

    @Test
    @CasoPrueba(
            id = "CP74",
            descripcion = "CustomUserDetailsService - lanza UsernameNotFoundException si el usuario no existe",
            entrada = "email=noexiste@test.com",
            tipo = "Negativa",
            esperado = "Se lanza UsernameNotFoundException"
    )
    void loadUserByUsername_notFound_throwsException() {
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("noexiste@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("noexiste@test.com"));

        verify(userRepository).findByEmail("noexiste@test.com");
        verify(userRepository).findByUsername("noexiste@test.com");

        System.out.println("CP74 UsernameNotFoundException lanzada correctamente para usuario inexistente");
    }
}
