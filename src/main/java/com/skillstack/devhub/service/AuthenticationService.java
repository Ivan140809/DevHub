package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.LoginResponseDTO;
import com.skillstack.devhub.dto.UserLoginDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class AuthenticationService {

    private static final List<Map.Entry<Predicate<String>, String>> PASSWORD_RULES = List.of(
            Map.entry(p -> p.length() < 6,               "DEBE TENER POR LO MENOS 6 CARACTERES"),
            Map.entry(p -> !p.matches(".*[A-Z].*"),      "DEBE CONTENER AL MENOS UNA MAYUSCULA"),
            Map.entry(p -> !p.matches(".*[a-z].*"),      "DEBE CONTENER AL MENOS UNA MINUSCULA"),
            Map.entry(p -> !p.matches(".*\\d.*"),         "DEBE CONTENER AL MENOS UN NUMERO"),
            Map.entry(p -> !p.matches(".*[@#$%^&+=!].*"), "DEBE CONTENER AL MENOS UN SIMBOLO (@#$%^&+=!)")
    );

    @Value("${admin.secret}")
    private String adminSecret;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final DefaultUserFactory defaultUserFactory;
    private final AdminUserFactory adminUserFactory;
    private final TwilioService twilioService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                                 DefaultUserFactory defaultUserFactory, AdminUserFactory adminUserFactory,
                                 TwilioService twilioService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.defaultUserFactory = defaultUserFactory;
        this.adminUserFactory = adminUserFactory;
        this.twilioService = twilioService;
    }

    private User findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("USUARIO CON EMAIL " + email + " NO ENCONTRADO"));
    }

    public String validatePassword(String password) {
        if (password == null) return "DEBE INGRESAR UNA CONTRASENA";
        return PASSWORD_RULES.stream()
                .filter(rule -> rule.getKey().test(password))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("OK");
    }

    private void assertPasswordValid(String password) {
        String result = validatePassword(password);
        if (!result.equals("OK")) {
            throw new PasswordFormatException("CONTRASENA " + result);
        }
    }

    public String register(UserRegisterDTO user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("EMAIL " + user.getEmail() + " YA EN USO");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("USERNAME " + user.getUsername() + " YA EN USO");
        }
        assertPasswordValid(user.getPassword());

        if (userRepository.count() == 0) {
            AdminUser ua = (AdminUser) adminUserFactory.createUser(user.getFirstName(), user.getLastName(),
                    user.getUsername(), user.getEmail(), user.getPassword(), user.getPhone(), Role.ADMIN);
            ua.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(ua);
        } else {
            User u = (User) defaultUserFactory.createUser(user.getFirstName(), user.getLastName(),
                    user.getUsername(), user.getEmail(), user.getPassword(), user.getPhone(), Role.USER);
            u.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(u);
        }
        return "USUARIO REGISTRADO CORRECTAMENTE";
    }

    public String promoteToAdmin(String email, String providedSecret) {
        if (!adminSecret.equals(providedSecret)) {
            throw new IncorrectPasswordException("CLAVE ADMIN INCORRECTA");
        }
        User user = findUserByEmailOrThrow(email);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return "USUARIO " + email + " PROMOVIDO A ADMIN CORRECTAMENTE";
    }

    public LoginResponseDTO login(UserLoginDTO request) {
        User user = findUserByEmailOrThrow(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("CONTRASENA INCORRECTA");
        }
        return new LoginResponseDTO(jwtUtil.generateToken(user.getEmail()));
    }

    public String requestPasswordReset(String email) {
        User user = findUserByEmailOrThrow(email);
        if (user.getPhone() == null || user.getPhone().isBlank()) {
            throw new IllegalStateException("EL USUARIO NO TIENE TELEFONO REGISTRADO");
        }
        twilioService.sendResetCode(user.getPhone(), email);
        return "CODIGO ENVIADO AL TELEFONO REGISTRADO";
    }

    public String resetPasswordWithCode(String email, String code, String newPassword) {
        if (!twilioService.verifyCode(email, code)) {
            throw new IncorrectPasswordException("CODIGO DE VERIFICACION INVALIDO O EXPIRADO");
        }
        return resetPassword(email, newPassword);
    }

    public boolean verifyCode(String twilioCode, String userCode) {
        return twilioCode.equals(userCode);
    }

    public String resetPassword(String email, String password) {
        User user = findUserByEmailOrThrow(email);
        assertPasswordValid(password);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "CONTRASENA CAMBIADA EXITOSAMENTE";
    }
}
