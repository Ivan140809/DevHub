package com.skillstack.devhub.exception;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class ExceptionsTest {

    @Test
    @CasoPrueba(
            id = "CP54",
            descripcion = "PasswordFormatException - mensaje de error propagado correctamente",
            entrada = "message=CONTRASENA DEBE TENER POR LO MENOS 6 CARACTERES",
            tipo = "Normal",
            esperado = "El mensaje de la excepcion es el esperado y es instancia de RuntimeException"
    )
    void passwordFormatException_message() {
        String msg = "CONTRASENA DEBE TENER POR LO MENOS 6 CARACTERES";
        PasswordFormatException ex = new PasswordFormatException(msg);

        assertEquals(msg, ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    @CasoPrueba(
            id = "CP55",
            descripcion = "ReviewNotFoundException - mensaje de error propagado correctamente",
            entrada = "message=REVIEW NO ENCONTRADA",
            tipo = "Normal",
            esperado = "El mensaje de la excepcion es el esperado y es instancia de RuntimeException"
    )
    void reviewNotFoundException_message() {
        String msg = "REVIEW NO ENCONTRADA";
        ReviewNotFoundException ex = new ReviewNotFoundException(msg);

        assertEquals(msg, ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    @CasoPrueba(
            id = "CP55B",
            descripcion = "ReviewNotFoundException - puede ser lanzada y capturada como RuntimeException",
            entrada = "message=REVIEW CON ID 99 NO ENCONTRADA",
            tipo = "Normal",
            esperado = "Se lanza ReviewNotFoundException y se captura como RuntimeException"
    )
    void reviewNotFoundException_canBeThrownAndCaught() {
        assertThrows(ReviewNotFoundException.class, () -> {
            throw new ReviewNotFoundException("REVIEW CON ID 99 NO ENCONTRADA");
        });
    }

    @Test
    @CasoPrueba(
            id = "CP54B",
            descripcion = "PasswordFormatException - puede ser lanzada y capturada",
            entrada = "message=CONTRASENA INVALIDA",
            tipo = "Negativa",
            esperado = "Se lanza PasswordFormatException y se captura correctamente"
    )
    void passwordFormatException_canBeThrownAndCaught() {
        assertThrows(PasswordFormatException.class, () -> {
            throw new PasswordFormatException("CONTRASENA INVALIDA");
        });
    }
}
