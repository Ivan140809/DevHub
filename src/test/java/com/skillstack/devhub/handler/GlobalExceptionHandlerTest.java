package com.skillstack.devhub.handler;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    @CasoPrueba(
            id = "CP62",
            descripcion = "ErrorResponse - constructor con argumentos y getters retornan valores correctos",
            entrada = "timestamp=now, status=404, message=NOT FOUND",
            tipo = "Normal",
            esperado = "Los getters retornan los valores del constructor"
    )
    void errorResponse_constructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse(now, 404, "NOT FOUND");

        assertEquals(now, response.getTimestamp());
        assertEquals(404, response.getStatus());
        assertEquals("NOT FOUND", response.getMessage());
    }

    @Test
    @CasoPrueba(
            id = "CP63",
            descripcion = "ErrorResponse - constructor vacio y setters actualizan los valores",
            entrada = "timestamp=now, status=500, message=ERROR INTERNO",
            tipo = "Normal",
            esperado = "Los setters actualizan los valores y los getters los retornan correctamente"
    )
    void errorResponse_emptyConstructorAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(now);
        response.setStatus(500);
        response.setMessage("ERROR INTERNO");

        assertEquals(now, response.getTimestamp());
        assertEquals(500, response.getStatus());
        assertEquals("ERROR INTERNO", response.getMessage());
    }

    @Test
    @CasoPrueba(
            id = "CP64",
            descripcion = "GlobalExceptionHandler - handleNotFound con UserNotFoundException retorna 404",
            entrada = "UserNotFoundException con mensaje USUARIO NO ENCONTRADO",
            tipo = "Normal",
            esperado = "ResponseEntity con status 404 y el mensaje de la excepcion"
    )
    void handleNotFound_userNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("USUARIO NO ENCONTRADO");
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("USUARIO NO ENCONTRADO", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @CasoPrueba(
            id = "CP65",
            descripcion = "GlobalExceptionHandler - handleNotFound con ReviewNotFoundException retorna 404",
            entrada = "ReviewNotFoundException con mensaje REVIEW NO ENCONTRADA",
            tipo = "Normal",
            esperado = "ResponseEntity con status 404 y el mensaje de la excepcion"
    )
    void handleNotFound_reviewNotFoundException() {
        ReviewNotFoundException ex = new ReviewNotFoundException("REVIEW NO ENCONTRADA");
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("REVIEW NO ENCONTRADA", response.getBody().getMessage());
    }

    @Test
    @CasoPrueba(
            id = "CP66",
            descripcion = "GlobalExceptionHandler - handleAlreadyExists con UserAlreadyExistsException retorna 409",
            entrada = "UserAlreadyExistsException con mensaje EMAIL YA EN USO",
            tipo = "Normal",
            esperado = "ResponseEntity con status 409 CONFLICT"
    )
    void handleAlreadyExists_userAlreadyExists() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("EMAIL YA EN USO");
        ResponseEntity<ErrorResponse> response = handler.handleAlreadyExists(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("EMAIL YA EN USO", response.getBody().getMessage());
    }

    @Test
    @CasoPrueba(
            id = "CP67",
            descripcion = "GlobalExceptionHandler - handlePasswordFormat retorna 400 BAD_REQUEST",
            entrada = "PasswordFormatException con mensaje CONTRASENA INVALIDA",
            tipo = "Normal",
            esperado = "ResponseEntity con status 400 BAD_REQUEST"
    )
    void handlePasswordFormat() {
        PasswordFormatException ex = new PasswordFormatException("CONTRASENA INVALIDA");
        ResponseEntity<ErrorResponse> response = handler.handlePasswordFormat(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("CONTRASENA INVALIDA", response.getBody().getMessage());
    }

    @Test
    @CasoPrueba(
            id = "CP68",
            descripcion = "GlobalExceptionHandler - handleIncorrectPassword retorna 401 UNAUTHORIZED",
            entrada = "IncorrectPasswordException con mensaje CONTRASENA INCORRECTA",
            tipo = "Normal",
            esperado = "ResponseEntity con status 401 UNAUTHORIZED"
    )
    void handleIncorrectPassword() {
        IncorrectPasswordException ex = new IncorrectPasswordException("CONTRASENA INCORRECTA");
        ResponseEntity<ErrorResponse> response = handler.handleIncorrectPassword(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getStatus());
        assertEquals("CONTRASENA INCORRECTA", response.getBody().getMessage());
    }

    @Test
    @CasoPrueba(
            id = "CP69",
            descripcion = "GlobalExceptionHandler - handleRuntimeException retorna 500 INTERNAL_SERVER_ERROR",
            entrada = "RuntimeException con mensaje ERROR INESPERADO",
            tipo = "Normal",
            esperado = "ResponseEntity con status 500 INTERNAL_SERVER_ERROR"
    )
    void handleRuntimeException() {
        RuntimeException ex = new RuntimeException("ERROR INESPERADO");
        ResponseEntity<ErrorResponse> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("ERROR INESPERADO", response.getBody().getMessage());
    }
}
