package com.skillstack.devhub.config;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    @CasoPrueba(
            id = "CP93",
            descripcion = "SecurityConfig - passwordEncoder crea un BCryptPasswordEncoder funcional",
            entrada = "password=Pass1!",
            tipo = "Normal",
            esperado = "El PasswordEncoder es BCrypt y puede codificar y verificar contrasenas"
    )
    void passwordEncoder_returnsBCrypt() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
        assertInstanceOf(BCryptPasswordEncoder.class, encoder);

        String rawPassword = "Pass1!";
        String encoded = encoder.encode(rawPassword);
        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
        assertTrue(encoder.matches(rawPassword, encoded));

        System.out.println("CP93 BCryptPasswordEncoder funciona correctamente");
        System.out.println("Encoded (parcial): " + encoded.substring(0, 10) + "...");
    }

    @Test
    @CasoPrueba(
            id = "CP93B",
            descripcion = "SecurityConfig - passwordEncoder no reconoce contrasena incorrecta",
            entrada = "password=Pass1!, wrongPassword=OtraPass!",
            tipo = "Negativa",
            esperado = "matches retorna false para la contrasena incorrecta"
    )
    void passwordEncoder_wrongPasswordDoesNotMatch() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded = encoder.encode("Pass1!");

        assertFalse(encoder.matches("OtraPass!", encoded));

        System.out.println("CP93B La contrasena incorrecta no hace match correctamente");
    }

    @Test
    @CasoPrueba(
            id = "CP94",
            descripcion = "SecurityConfig - corsConfigurationSource retorna una fuente de configuracion no nula",
            entrada = "Sin parametros",
            tipo = "Normal",
            esperado = "CorsConfigurationSource no es nulo"
    )
    void corsConfigurationSource_isNotNull() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();

        assertNotNull(source);

        System.out.println("CP94 CorsConfigurationSource creado correctamente");
    }
}
