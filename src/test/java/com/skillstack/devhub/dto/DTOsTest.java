package com.skillstack.devhub.dto;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class DTOsTest {

    @Test
    @CasoPrueba(
            id = "CP50",
            descripcion = "ForgotPasswordDTO - getter y setter de email",
            entrada = "email=test@test.com",
            tipo = "Normal",
            esperado = "getEmail retorna el email seteado"
    )
    void forgotPasswordDTO_getterSetter() {
        ForgotPasswordDTO dto = new ForgotPasswordDTO();
        dto.setEmail("test@test.com");

        assertEquals("test@test.com", dto.getEmail());
    }

    @Test
    @CasoPrueba(
            id = "CP51",
            descripcion = "PromoteAdminDTO - getters y setters de email y adminKey",
            entrada = "email=admin@test.com",
            tipo = "Normal",
            esperado = "getEmail retorna el valor seteado, getAdminKey retorna null si no fue seteado"
    )
    void promoteAdminDTO_gettersSetters() {
        PromoteAdminDTO dto = new PromoteAdminDTO();
        dto.setEmail("admin@test.com");

        assertEquals("admin@test.com", dto.getEmail());
        assertNull(dto.getAdminKey());
    }

    @Test
    @CasoPrueba(
            id = "CP52",
            descripcion = "ResetPasswordDTO - getters y setters de todos los campos",
            entrada = "email=test@test.com, code=123456, newPassword=NewPass1!",
            tipo = "Normal",
            esperado = "Los getters retornan los valores seteados correctamente"
    )
    void resetPasswordDTO_gettersSetters() {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setEmail("test@test.com");
        dto.setCode("123456");
        dto.setNewPassword("NewPass1!");

        assertEquals("test@test.com", dto.getEmail());
        assertEquals("123456", dto.getCode());
        assertEquals("NewPass1!", dto.getNewPassword());
    }

    @Test
    @CasoPrueba(
            id = "CP53",
            descripcion = "ProgressDTO - constructor con totalAnswered y percentage crea el objeto",
            entrada = "totalAnswered=10, percentage=75.0",
            tipo = "Normal",
            esperado = "El objeto ProgressDTO se crea correctamente sin lanzar excepciones"
    )
    void progressDTO_constructor() {
        ProgressDTO dto = new ProgressDTO(10, 75.0);

        assertNotNull(dto);
    }

    @Test
    @CasoPrueba(
            id = "CP53B",
            descripcion = "ProgressDTO - constructor con valores borde: totalAnswered=0 y percentage=0.0",
            entrada = "totalAnswered=0, percentage=0.0",
            tipo = "Borde",
            esperado = "El objeto ProgressDTO se crea correctamente con valores en cero"
    )
    void progressDTO_constructor_borderValues() {
        ProgressDTO dto = new ProgressDTO(0, 0.0);

        assertNotNull(dto);
    }
}
