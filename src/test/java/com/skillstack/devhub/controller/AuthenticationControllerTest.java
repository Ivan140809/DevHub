package com.skillstack.devhub.controller;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.*;
import com.skillstack.devhub.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    @CasoPrueba(
            id = "CP85",
            descripcion = "AuthenticationController - registerUser retorna 201 CREATED con mensaje de exito",
            entrada = "UserRegisterDTO con datos validos",
            tipo = "Normal",
            esperado = "ResponseEntity con status 201 y mensaje USUARIO REGISTRADO CORRECTAMENTE"
    )
    void registerUser_returns201() {
        UserRegisterDTO dto = new UserRegisterDTO();
        when(authenticationService.register(dto)).thenReturn("USUARIO REGISTRADO CORRECTAMENTE");

        ResponseEntity<String> response = authenticationController.registerUser(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("USUARIO REGISTRADO CORRECTAMENTE", response.getBody());
        verify(authenticationService).register(dto);

        System.out.println("CP85 registerUser retorno 201: " + response.getBody());
    }

    @Test
    @CasoPrueba(
            id = "CP86",
            descripcion = "AuthenticationController - login retorna 200 OK con token JWT",
            entrada = "email=test@test.com, password=Pass1!",
            tipo = "Normal",
            esperado = "ResponseEntity con status 200 y LoginResponseDTO con token"
    )
    void login_returns200WithToken() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("Pass1!");

        LoginResponseDTO loginResponse = new LoginResponseDTO("token123");
        when(authenticationService.login(dto)).thenReturn(loginResponse);

        ResponseEntity<LoginResponseDTO> response = authenticationController.login(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token123", response.getBody().getToken());
        verify(authenticationService).login(dto);

        System.out.println("CP86 login retorno token: " + response.getBody().getToken());
    }

    @Test
    @CasoPrueba(
            id = "CP87",
            descripcion = "AuthenticationController - promoteToAdmin retorna 200 OK con mensaje de confirmacion",
            entrada = "email=admin@test.com, adminKey=null",
            tipo = "Normal",
            esperado = "ResponseEntity con status 200 y mensaje de promocion"
    )
    void promoteToAdmin_returns200() {
        PromoteAdminDTO dto = new PromoteAdminDTO();
        dto.setEmail("admin@test.com");

        when(authenticationService.promoteToAdmin("admin@test.com", null))
                .thenReturn("USUARIO PROMOVIDO A ADMIN");

        ResponseEntity<String> response = authenticationController.promoteToAdmin(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("USUARIO PROMOVIDO A ADMIN", response.getBody());
        verify(authenticationService).promoteToAdmin("admin@test.com", null);

        System.out.println("CP87 promoteToAdmin: " + response.getBody());
    }

    @Test
    @CasoPrueba(
            id = "CP88",
            descripcion = "AuthenticationController - forgotPassword retorna 200 OK con mensaje de confirmacion",
            entrada = "email=test@test.com",
            tipo = "Normal",
            esperado = "ResponseEntity con status 200 y mensaje CODIGO ENVIADO AL TELEFONO REGISTRADO"
    )
    void forgotPassword_returns200() {
        ForgotPasswordDTO dto = new ForgotPasswordDTO();
        dto.setEmail("test@test.com");

        when(authenticationService.requestPasswordReset("test@test.com"))
                .thenReturn("CODIGO ENVIADO AL TELEFONO REGISTRADO");

        ResponseEntity<String> response = authenticationController.forgotPassword(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("CODIGO ENVIADO AL TELEFONO REGISTRADO", response.getBody());
        verify(authenticationService).requestPasswordReset("test@test.com");

        System.out.println("CP88 forgotPassword: " + response.getBody());
    }

    @Test
    @CasoPrueba(
            id = "CP89",
            descripcion = "AuthenticationController - resetPassword retorna 200 OK con mensaje de exito",
            entrada = "email=test@test.com, code=123456, newPassword=NewPass1!",
            tipo = "Normal",
            esperado = "ResponseEntity con status 200 y mensaje CONTRASENA CAMBIADA EXITOSAMENTE"
    )
    void resetPassword_returns200() {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setEmail("test@test.com");
        dto.setCode("123456");
        dto.setNewPassword("NewPass1!");

        when(authenticationService.resetPasswordWithCode("test@test.com", "123456", "NewPass1!"))
                .thenReturn("CONTRASENA CAMBIADA EXITOSAMENTE");

        ResponseEntity<String> response = authenticationController.resetPassword(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("CONTRASENA CAMBIADA EXITOSAMENTE", response.getBody());
        verify(authenticationService).resetPasswordWithCode("test@test.com", "123456", "NewPass1!");

        System.out.println("CP89 resetPassword: " + response.getBody());
    }
}
