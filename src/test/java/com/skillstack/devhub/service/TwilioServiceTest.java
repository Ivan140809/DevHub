package com.skillstack.devhub.service;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class TwilioServiceTest {

    @SuppressWarnings("unchecked")
    private Map<String, String> getPendingCodes(TwilioService service) throws Exception {
        Field field = TwilioService.class.getDeclaredField("pendingCodes");
        field.setAccessible(true);
        return (Map<String, String>) field.get(service);
    }

    @Test
    @CasoPrueba(
            id = "CP80",
            descripcion = "TwilioService - verifyCode retorna true y elimina el codigo cuando es correcto",
            entrada = "email=test@test.com, code=123456",
            tipo = "Normal",
            esperado = "verifyCode retorna true y el codigo es eliminado del mapa pendingCodes"
    )
    void verifyCode_correctCode_returnsTrue() throws Exception {
        TwilioService service = new TwilioService();
        Map<String, String> codes = getPendingCodes(service);
        codes.put("test@test.com", "123456");

        boolean result = service.verifyCode("test@test.com", "123456");

        assertTrue(result);
        assertFalse(codes.containsKey("test@test.com"),
                "El codigo debe ser eliminado tras una verificacion exitosa");

        System.out.println("CP80 verifyCode correcto: retorno " + result);
    }

    @Test
    @CasoPrueba(
            id = "CP81",
            descripcion = "TwilioService - verifyCode retorna false cuando el codigo es incorrecto",
            entrada = "email=test@test.com, codeGuardado=123456, codeIngresado=999999",
            tipo = "Negativa",
            esperado = "verifyCode retorna false y el codigo permanece en pendingCodes"
    )
    void verifyCode_incorrectCode_returnsFalse() throws Exception {
        TwilioService service = new TwilioService();
        Map<String, String> codes = getPendingCodes(service);
        codes.put("test@test.com", "123456");

        boolean result = service.verifyCode("test@test.com", "999999");

        assertFalse(result);
        assertTrue(codes.containsKey("test@test.com"),
                "El codigo no debe ser eliminado si la verificacion falla");

        System.out.println("CP81 verifyCode incorrecto: retorno " + result);
    }

    @Test
    @CasoPrueba(
            id = "CP82",
            descripcion = "TwilioService - verifyCode retorna false si no hay codigo pendiente para el email",
            entrada = "email=sincodigo@test.com",
            tipo = "Negativa",
            esperado = "verifyCode retorna false"
    )
    void verifyCode_noCodeForEmail_returnsFalse() {
        TwilioService service = new TwilioService();

        boolean result = service.verifyCode("sincodigo@test.com", "123456");

        assertFalse(result);

        System.out.println("CP82 verifyCode sin codigo pendiente: retorno " + result);
    }

    @Test
    @CasoPrueba(
            id = "CP82B",
            descripcion = "TwilioService - dos verificaciones seguidas con el mismo codigo, la segunda falla",
            entrada = "email=test@test.com, code=123456",
            tipo = "Logica Negocio",
            esperado = "Primera verificacion retorna true, segunda retorna false (codigo eliminado)"
    )
    void verifyCode_secondAttemptFails() throws Exception {
        TwilioService service = new TwilioService();
        Map<String, String> codes = getPendingCodes(service);
        codes.put("test@test.com", "123456");

        boolean first = service.verifyCode("test@test.com", "123456");
        boolean second = service.verifyCode("test@test.com", "123456");

        assertTrue(first, "La primera verificacion debe ser exitosa");
        assertFalse(second, "La segunda verificacion debe fallar porque el codigo fue eliminado");

        System.out.println("CP82B Primera verificacion: " + first + ", Segunda: " + second);
    }
}
