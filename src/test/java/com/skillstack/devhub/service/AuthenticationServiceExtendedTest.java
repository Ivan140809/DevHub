package com.skillstack.devhub.service;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.UserRegisterDTO;
import com.skillstack.devhub.exception.IncorrectPasswordException;
import com.skillstack.devhub.exception.PasswordFormatException;
import com.skillstack.devhub.exception.UserAlreadyExistsException;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.factorymethod.AdminUserFactory;
import com.skillstack.devhub.factorymethod.DefaultUserFactory;
import com.skillstack.devhub.model.AdminUser;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
import com.skillstack.devhub.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class AuthenticationServiceExtendedTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private DefaultUserFactory defaultUserFactory;
    @Mock private AdminUserFactory adminUserFactory;
    @Mock private TwilioService twilioService;

    @InjectMocks
    private AuthenticationService service;

    private void setAdminSecret(String secret) {
        ReflectionTestUtils.setField(service, "adminSecret", secret);
    }

    // ======================== validatePassword ========================

    @Test
    @CasoPrueba(
            id = "CP300",
            descripcion = "validatePassword - contraseña nula retorna mensaje de error",
            entrada = "password=null",
            tipo = "Negativa",
            esperado = "Retorna 'DEBE INGRESAR UNA CONTRASENA'"
    )
    void validatePassword_null_returnsErrorMessage() {
        String result = service.validatePassword(null);
        assertEquals("DEBE INGRESAR UNA CONTRASENA", result);
    }

    @Test
    @CasoPrueba(
            id = "CP301",
            descripcion = "validatePassword - contraseña de 5 chars retorna error de longitud mínima",
            entrada = "password=Ab1!x",
            tipo = "Negativa",
            esperado = "Retorna mensaje de mínimo 6 caracteres"
    )
    void validatePassword_tooShort_returnsLengthError() {
        String result = service.validatePassword("Ab1!x");
        assertEquals("DEBE TENER POR LO MENOS 6 CARACTERES", result);
    }

    @Test
    @CasoPrueba(
            id = "CP302",
            descripcion = "validatePassword - sin mayúscula retorna mensaje de error",
            entrada = "password=abc123!",
            tipo = "Negativa",
            esperado = "Retorna mensaje de falta de mayúscula"
    )
    void validatePassword_noUppercase_returnsError() {
        String result = service.validatePassword("abc123!");
        assertEquals("DEBE CONTENER AL MENOS UNA MAYUSCULA", result);
    }

    @Test
    @CasoPrueba(
            id = "CP303",
            descripcion = "validatePassword - sin minúscula retorna mensaje de error",
            entrada = "password=ABC123!",
            tipo = "Negativa",
            esperado = "Retorna mensaje de falta de minúscula"
    )
    void validatePassword_noLowercase_returnsError() {
        String result = service.validatePassword("ABC123!");
        assertEquals("DEBE CONTENER AL MENOS UNA MINUSCULA", result);
    }

    @Test
    @CasoPrueba(
            id = "CP304",
            descripcion = "validatePassword - sin número retorna mensaje de error",
            entrada = "password=Abcdef!",
            tipo = "Negativa",
            esperado = "Retorna mensaje de falta de número"
    )
    void validatePassword_noDigit_returnsError() {
        String result = service.validatePassword("Abcdef!");
        assertEquals("DEBE CONTENER AL MENOS UN NUMERO", result);
    }

    @Test
    @CasoPrueba(
            id = "CP305",
            descripcion = "validatePassword - sin símbolo retorna mensaje de error",
            entrada = "password=Abcdef1",
            tipo = "Negativa",
            esperado = "Retorna mensaje de falta de símbolo"
    )
    void validatePassword_noSymbol_returnsError() {
        String result = service.validatePassword("Abcdef1");
        assertEquals("DEBE CONTENER AL MENOS UN SIMBOLO (@#$%^&+=!)", result);
    }

    @Test
    @CasoPrueba(
            id = "CP306",
            descripcion = "validatePassword - contraseña válida con todos los requisitos retorna OK",
            entrada = "password=Valid1!",
            tipo = "Normal",
            esperado = "Retorna 'OK'"
    )
    void validatePassword_valid_returnsOK() {
        String result = service.validatePassword("Valid1!");
        assertEquals("OK", result);
    }

    @Test
    @CasoPrueba(
            id = "CP307",
            descripcion = "validatePassword - contraseña con símbolo # retorna OK",
            entrada = "password=Valid1#",
            tipo = "Normal",
            esperado = "Retorna 'OK'"
    )
    void validatePassword_symbolHash_returnsOK() {
        assertEquals("OK", service.validatePassword("Valid1#"));
    }

    @Test
    @CasoPrueba(
            id = "CP308",
            descripcion = "validatePassword - contraseña vacía retorna error de longitud",
            entrada = "password=''",
            tipo = "Borde",
            esperado = "Retorna mensaje de mínimo 6 caracteres"
    )
    void validatePassword_empty_returnsTooShortError() {
        String result = service.validatePassword("");
        assertEquals("DEBE TENER POR LO MENOS 6 CARACTERES", result);
    }

    // ======================== promoteToAdmin ========================

    @Test
    @CasoPrueba(
            id = "CP309",
            descripcion = "promoteToAdmin - clave incorrecta lanza IncorrectPasswordException",
            entrada = "email=user@test.com, secret=wrongSecret",
            tipo = "Negativa",
            esperado = "IncorrectPasswordException lanzada, userRepository.save nunca es llamado"
    )
    void promoteToAdmin_wrongSecret_throwsIncorrectPasswordException() {
        setAdminSecret("correctSecret");
        assertThrows(IncorrectPasswordException.class,
                () -> service.promoteToAdmin("user@test.com", "wrongSecret"));
        verify(userRepository, never()).save(any());
    }

    @Test
    @CasoPrueba(
            id = "CP310",
            descripcion = "promoteToAdmin - usuario no encontrado lanza UserNotFoundException",
            entrada = "email=noexiste@test.com, secret=correctSecret",
            tipo = "Negativa",
            esperado = "UserNotFoundException lanzada"
    )
    void promoteToAdmin_userNotFound_throwsUserNotFoundException() {
        setAdminSecret("correctSecret");
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> service.promoteToAdmin("noexiste@test.com", "correctSecret"));
    }

    @Test
    @CasoPrueba(
            id = "CP311",
            descripcion = "promoteToAdmin - clave correcta y usuario existente promueve correctamente",
            entrada = "email=user@test.com, secret=correctSecret",
            tipo = "Normal",
            esperado = "El usuario tiene rol ADMIN y se retorna mensaje de éxito"
    )
    void promoteToAdmin_success() {
        setAdminSecret("correctSecret");
        User user = new User();
        user.setEmail("user@test.com");
        user.setRole(Role.USER);
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        String result = service.promoteToAdmin("user@test.com", "correctSecret");

        assertEquals("USUARIO user@test.com PROMOVIDO A ADMIN CORRECTAMENTE", result);
        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository).save(user);
    }

    // ======================== requestPasswordReset ========================

    @Test
    @CasoPrueba(
            id = "CP312",
            descripcion = "requestPasswordReset - usuario con phone=null lanza IllegalStateException",
            entrada = "email=user@test.com, phone=null",
            tipo = "Negativa",
            esperado = "IllegalStateException lanzada"
    )
    void requestPasswordReset_nullPhone_throwsIllegalStateException() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setPhone(null);
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class,
                () -> service.requestPasswordReset("user@test.com"));
    }

    @Test
    @CasoPrueba(
            id = "CP313",
            descripcion = "requestPasswordReset - usuario con phone en blanco lanza IllegalStateException",
            entrada = "email=user@test.com, phone='   '",
            tipo = "Negativa",
            esperado = "IllegalStateException lanzada"
    )
    void requestPasswordReset_blankPhone_throwsIllegalStateException() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setPhone("   ");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class,
                () -> service.requestPasswordReset("user@test.com"));
    }

    @Test
    @CasoPrueba(
            id = "CP314",
            descripcion = "requestPasswordReset - usuario no encontrado lanza UserNotFoundException",
            entrada = "email=noexiste@test.com",
            tipo = "Negativa",
            esperado = "UserNotFoundException lanzada"
    )
    void requestPasswordReset_userNotFound_throwsUserNotFoundException() {
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> service.requestPasswordReset("noexiste@test.com"));
    }

    @Test
    @CasoPrueba(
            id = "CP315",
            descripcion = "requestPasswordReset - usuario con teléfono válido envía el código",
            entrada = "email=user@test.com, phone=3001234567",
            tipo = "Normal",
            esperado = "Retorna 'CODIGO ENVIADO AL TELEFONO REGISTRADO' y sendResetCode es llamado"
    )
    void requestPasswordReset_success() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setPhone("3001234567");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        doNothing().when(twilioService).sendResetCode(anyString(), anyString());

        String result = service.requestPasswordReset("user@test.com");

        assertEquals("CODIGO ENVIADO AL TELEFONO REGISTRADO", result);
        verify(twilioService).sendResetCode("3001234567", "user@test.com");
    }

    // ======================== resetPasswordWithCode ========================

    @Test
    @CasoPrueba(
            id = "CP316",
            descripcion = "resetPasswordWithCode - código inválido lanza IncorrectPasswordException",
            entrada = "email=user@test.com, code=999999",
            tipo = "Negativa",
            esperado = "IncorrectPasswordException lanzada"
    )
    void resetPasswordWithCode_invalidCode_throwsIncorrectPasswordException() {
        when(twilioService.verifyCode("user@test.com", "999999")).thenReturn(false);
        assertThrows(IncorrectPasswordException.class,
                () -> service.resetPasswordWithCode("user@test.com", "999999", "NewPass1!"));
    }

    @Test
    @CasoPrueba(
            id = "CP317",
            descripcion = "resetPasswordWithCode - código válido actualiza la contraseña correctamente",
            entrada = "email=user@test.com, code=123456, newPassword=NewPass1!",
            tipo = "Normal",
            esperado = "Contraseña actualizada, retorna 'CONTRASENA CAMBIADA EXITOSAMENTE'"
    )
    void resetPasswordWithCode_validCode_updatesPassword() {
        User user = new User();
        user.setEmail("user@test.com");
        when(twilioService.verifyCode("user@test.com", "123456")).thenReturn(true);
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("NewPass1!")).thenReturn("encodedNew");

        String result = service.resetPasswordWithCode("user@test.com", "123456", "NewPass1!");

        assertEquals("CONTRASENA CAMBIADA EXITOSAMENTE", result);
        assertEquals("encodedNew", user.getPassword());
        verify(userRepository).save(user);
    }

    // ======================== resetPassword ========================

    @Test
    @CasoPrueba(
            id = "CP318",
            descripcion = "resetPassword - contraseña sin formato válido lanza PasswordFormatException",
            entrada = "email=user@test.com, password=debil",
            tipo = "Negativa",
            esperado = "PasswordFormatException lanzada"
    )
    void resetPassword_invalidPassword_throwsPasswordFormatException() {
        User user = new User();
        user.setEmail("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        assertThrows(PasswordFormatException.class,
                () -> service.resetPassword("user@test.com", "debil"));
    }

    @Test
    @CasoPrueba(
            id = "CP319",
            descripcion = "resetPassword - contraseña válida actualiza correctamente",
            entrada = "email=user@test.com, password=NewPass1!",
            tipo = "Normal",
            esperado = "Contraseña actualizada, retorna 'CONTRASENA CAMBIADA EXITOSAMENTE'"
    )
    void resetPassword_validPassword_updatesSuccessfully() {
        User user = new User();
        user.setEmail("user@test.com");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("NewPass1!")).thenReturn("encodedNew");

        String result = service.resetPassword("user@test.com", "NewPass1!");

        assertEquals("CONTRASENA CAMBIADA EXITOSAMENTE", result);
        assertEquals("encodedNew", user.getPassword());
        verify(userRepository).save(user);
    }

    // ======================== register (casos adicionales) ========================

    @Test
    @CasoPrueba(
            id = "CP320",
            descripcion = "register - primer usuario (count=0) usa AdminUserFactory",
            entrada = "userRepository.count()=0",
            tipo = "Logica Negocio",
            esperado = "adminUserFactory.createUser es llamado, el usuario se guarda"
    )
    void register_firstUserBecomesAdmin() {
        UserRegisterDTO dto = new UserRegisterDTO("Ivan", "L", "ivan", "ivan@test.com", "Pass1!", "123");
        AdminUser adminUser = new AdminUser("Ivan", "L", "ivan", "ivan@test.com", "Pass1!", "123", Role.ADMIN);
        when(userRepository.count()).thenReturn(0L);
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
        when(adminUserFactory.createUser(any(), any(), any(), any(), any(), any(), any())).thenReturn(adminUser);
        when(passwordEncoder.encode("Pass1!")).thenReturn("encoded");

        String result = service.register(dto);

        assertEquals("USUARIO REGISTRADO CORRECTAMENTE", result);
        verify(adminUserFactory).createUser(any(), any(), any(), any(), any(), any(), any());
        verify(userRepository).save(adminUser);
    }

    @Test
    @CasoPrueba(
            id = "CP321",
            descripcion = "register - username ya existe lanza UserAlreadyExistsException",
            entrada = "email nuevo, username=existingUser ya registrado",
            tipo = "Negativa",
            esperado = "UserAlreadyExistsException lanzada, save nunca es llamado"
    )
    void register_usernameAlreadyExists_throwsException() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setEmail("nuevo@test.com");
        dto.setUsername("existingUser");
        when(userRepository.findByEmail("nuevo@test.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> service.register(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    @CasoPrueba(
            id = "CP322",
            descripcion = "register - contraseña sin símbolo lanza PasswordFormatException",
            entrada = "email nuevo, username nuevo, password=NoSymbol1",
            tipo = "Negativa",
            esperado = "PasswordFormatException lanzada"
    )
    void register_invalidPasswordNoSymbol_throwsPasswordFormatException() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setEmail("nuevo@test.com");
        dto.setUsername("newuser");
        dto.setPassword("NoSymbol1");
        when(userRepository.findByEmail("nuevo@test.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());

        assertThrows(PasswordFormatException.class, () -> service.register(dto));
        verify(userRepository, never()).save(any());
    }

    // ======================== verifyCode ========================

    @Test
    @CasoPrueba(
            id = "CP323",
            descripcion = "verifyCode - mismos códigos retorna true",
            entrada = "twilioCode=123456, userCode=123456",
            tipo = "Normal",
            esperado = "true"
    )
    void verifyCode_sameCodes_returnsTrue() {
        assertTrue(service.verifyCode("123456", "123456"));
    }

    @Test
    @CasoPrueba(
            id = "CP324",
            descripcion = "verifyCode - códigos distintos retorna false",
            entrada = "twilioCode=123456, userCode=999999",
            tipo = "Negativa",
            esperado = "false"
    )
    void verifyCode_differentCodes_returnsFalse() {
        assertFalse(service.verifyCode("123456", "999999"));
    }
}
