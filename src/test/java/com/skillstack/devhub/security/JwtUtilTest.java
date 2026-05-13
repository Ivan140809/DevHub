package com.skillstack.devhub.security;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("rAZh5SYLNvlY7Z8c/WCFi8Z2oYznXFgdGe7UxMouISw=");
        jwtUtil.init();
    }

    @Test
    @CasoPrueba(
            id = "CP70",
            descripcion = "JwtUtil - generateToken genera un token JWT no nulo ni vacio",
            entrada = "email=test@test.com",
            tipo = "Normal",
            esperado = "El token generado es no nulo y no vacio"
    )
    void generateToken_returnsNonNullToken() {
        String token = jwtUtil.generateToken("test@test.com");

        assertNotNull(token);
        assertFalse(token.isEmpty());

        System.out.println("CP70 Token generado correctamente");
        System.out.println("Token (parcial): " + token.substring(0, Math.min(20, token.length())) + "...");
    }

    @Test
    @CasoPrueba(
            id = "CP71",
            descripcion = "JwtUtil - extractEmail extrae correctamente el email del token generado",
            entrada = "email=test@test.com",
            tipo = "Normal",
            esperado = "El email extraido del token es igual al email original"
    )
    void extractEmail_returnsCorrectEmail() {
        String email = "test@test.com";
        String token = jwtUtil.generateToken(email);
        String extracted = jwtUtil.extractEmail(token);

        assertEquals(email, extracted);

        System.out.println("CP71 Email extraido correctamente: " + extracted);
    }

    @Test
    @CasoPrueba(
            id = "CP71B",
            descripcion = "JwtUtil - tokens generados para distintos emails son diferentes",
            entrada = "email1=user1@test.com, email2=user2@test.com",
            tipo = "Normal",
            esperado = "Los dos tokens son distintos entre si"
    )
    void generateToken_differentEmailsProduceDifferentTokens() {
        String token1 = jwtUtil.generateToken("user1@test.com");
        String token2 = jwtUtil.generateToken("user2@test.com");

        assertNotEquals(token1, token2);

        assertEquals("user1@test.com", jwtUtil.extractEmail(token1));
        assertEquals("user2@test.com", jwtUtil.extractEmail(token2));
    }

    @Test
    @CasoPrueba(
            id = "CP71C",
            descripcion = "JwtUtil - token invalido lanza excepcion al extraer email",
            entrada = "token=esto.no.es.un.jwt",
            tipo = "Negativa",
            esperado = "Lanza una excepcion al intentar extraer el email de un token invalido"
    )
    void extractEmail_invalidToken_throwsException() {
        assertThrows(Exception.class, () -> jwtUtil.extractEmail("esto.no.es.un.jwt"));
    }
}
