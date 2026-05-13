package com.skillstack.devhub.controller;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.RankingDTO;
import com.skillstack.devhub.dto.UserResponseDTO;
import com.skillstack.devhub.dto.UserUpdateDTO;
import com.skillstack.devhub.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    // CP15 - Normal:Actualizar perfil de usuario con datos validos
    @Test
    @CasoPrueba(
            id = "CP15",
            descripcion = "Actualizar perfil de usuario con datos validos",
            entrada = "usuario=ivancambiameamessi@devhub.com",
            tipo = "Normal",
            esperado = "Retorna 200 OK con el UserResponseDTO actualizado"
    )
    void updateUser() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        UserResponseDTO expectedDTO = new UserResponseDTO();

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("ivancambiameamessi@devhub.com");
        when(userService.updateUser("ivancambiameamessi@devhub.com", updateDTO)).thenReturn(expectedDTO);

        ResponseEntity<UserResponseDTO> response = userController.updateUser(principal, updateDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("Perfil actualizado correctamente:");
        System.out.println("Status: " + response.getStatusCode());

        verify(userService).updateUser("ivancambiameamessi@devhub.com", updateDTO);
    }


    // CP16 - Normal: Obtener perfil de usuario autenticado
    @Test
    @CasoPrueba(
            id = "CP16",
            descripcion = "Obtener perfil del usuario autenticado",
            entrada = "email=ivan@devhub.com",
            tipo = "Normal",
            esperado = "Retorna el UserResponseDTO del usuario"
    )
    void getProfile() {
        UserResponseDTO expectedDTO = new UserResponseDTO();

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("ivan@devhub.com");
        when(userService.getProfile("ivan@devhub.com")).thenReturn(expectedDTO);

        ResponseEntity<UserResponseDTO> response = userController.getProfile(principal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("CP16 Perfil obtenido correctamente:");
        System.out.println("Status: " + response.getStatusCode());

        verify(userService).getProfile("ivan@devhub.com");
    }


    // CP17 - Normal: Obtener RankingDTO
    @Test
    @CasoPrueba(
            id = "CP17",
            descripcion = "Obtener ranking de usuarios",
            entrada = "RankingDTO r1 y r2",
            tipo = "Normal",
            esperado = "Retorna la lista de RankingDTO"
    )
    void getRanking() {
        RankingDTO r1 = new RankingDTO();
        RankingDTO r2 = new RankingDTO();
        when(userService.findRanking()).thenReturn(List.of(r1, r2));

        ResponseEntity<List<RankingDTO>> response = userController.getRanking();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        System.out.println("CP17 Ranking obtenido correctamente:");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Cantidad de usuarios: " + response.getBody().size());

        verify(userService).findRanking();
    }


    // CP18 - Normal: Eliminar cuenta de usuario
    @Test
    @CasoPrueba(
            id = "CP18",
            descripcion = "Eliminar cuenta del usuario",
            entrada = "usuario=fetuccini@devhub.com, id=1",
            tipo = "Normal",
            esperado = "Retorna 204 NO_CONTENT"
    )
    void deleteAccount() {
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId("1");

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("fetuccini@devhub.com");
        when(userService.getProfile("fetuccini@devhub.com")).thenReturn(userDTO);

        ResponseEntity<Void> response = userController.deleteAccount(principal);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        System.out.println("Cuenta eliminada correctamente:");
        System.out.println("Status: " + response.getStatusCode());

        verify(userService).getProfile("fetuccini@devhub.com");
        verify(userService).deleteAccount("1");
    }


    // CP19 - Normal: Convertir usuario a ADMIN
    @Test
    @CasoPrueba(
            id = "CP19",
            descripcion = "Promover usuario a rol ADMIN",
            entrada = "email=elpropioadmin@devhub.com",
            tipo = "Normal",
            esperado = "Retorna el mensaje de confirmacion de promoción a ADMIN"
    )
    void promoteUserToAdminReturns200() {
        ResponseEntity<String> response = userController.promoteUser("elpropioadmin@devhub.com");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario elpropioadmin@devhub.com promovido a ADMIN", response.getBody());

        System.out.println("CP19 Usuario promovido a ADMIN:");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Mensaje: " + response.getBody());

        verify(userService).promoteToAdmin("elpropioadmin@devhub.com");
    }
}
