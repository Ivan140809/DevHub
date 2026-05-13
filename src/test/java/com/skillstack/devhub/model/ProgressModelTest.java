package com.skillstack.devhub.model;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class ProgressModelTest {

    @Test
    @CasoPrueba(
            id = "CP56",
            descripcion = "Progress - setters y getters de totalAnswered y percentage",
            entrada = "totalAnswered=5, percentage=50.0",
            tipo = "Normal",
            esperado = "Los getters retornan los valores seteados"
    )
    void progress_settersGetters() {
        Progress progress = new Progress();
        progress.setTotalAnswered(5);
        progress.setPercentage(50.0);

        assertEquals(5, progress.getTotalAnswered());
        assertEquals(50.0, progress.getPercentage());
    }

    @Test
    @CasoPrueba(
            id = "CP56B",
            descripcion = "Progress - valores por defecto son cero",
            entrada = "Progress recien creado",
            tipo = "Borde",
            esperado = "totalAnswered=0 y percentage=0.0 por defecto"
    )
    void progress_defaultValues() {
        Progress progress = new Progress();

        assertEquals(0, progress.getTotalAnswered());
        assertEquals(0.0, progress.getPercentage());
    }

    @Test
    @CasoPrueba(
            id = "CP57",
            descripcion = "AdminUser - constructor inicializa todos los campos correctamente",
            entrada = "firstName=John, lastName=Doe, username=johndoe, email=john@test.com, password=Pass1!, phone=123, role=ADMIN",
            tipo = "Normal",
            esperado = "El AdminUser creado tiene los campos con los valores del constructor"
    )
    void adminUser_constructor() {
        AdminUser adminUser = new AdminUser(
                "John", "Doe", "johndoe",
                "john@test.com", "Pass1!", "123", Role.ADMIN
        );

        assertEquals("John", adminUser.getFirstName());
        assertEquals("Doe", adminUser.getLastName());
        assertEquals("johndoe", adminUser.getUsername());
        assertEquals("john@test.com", adminUser.getEmail());
        assertEquals("Pass1!", adminUser.getPassword());
        assertEquals("123", adminUser.getPhone());
        assertEquals(Role.ADMIN, adminUser.getRole());
    }

    @Test
    @CasoPrueba(
            id = "CP57B",
            descripcion = "AdminUser - hereda setters de AbstractUser",
            entrada = "email=nuevo@test.com",
            tipo = "Normal",
            esperado = "El AdminUser puede actualizar email mediante setEmail heredado"
    )
    void adminUser_inheritedSetters() {
        AdminUser adminUser = new AdminUser(
                "John", "Doe", "johndoe",
                "john@test.com", "Pass1!", "123", Role.ADMIN
        );
        adminUser.setEmail("nuevo@test.com");

        assertEquals("nuevo@test.com", adminUser.getEmail());
    }
}
