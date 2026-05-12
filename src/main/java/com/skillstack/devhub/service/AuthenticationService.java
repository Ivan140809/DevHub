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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private static final String USER_NOT_FOUND = "USER NO ENCONTRADO";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final DefaultUserFactory defaultUserFactory;
    private final AdminUserFactory adminUserFactory;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, DefaultUserFactory defaultUserFactory, AdminUserFactory adminUserFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.defaultUserFactory = defaultUserFactory;
        this.adminUserFactory = adminUserFactory;
    }

    public String validatePassword(String password) {

        if(password.length()<6){
            return "DEBE TENER POR LO MENOS 6 CARACTERES";

        }

        if (!password.matches(".*[A-Z].*")) {
            return " DEBE CONTENER AL MENOS UNA MAYUSCULA";
        }

        if (!password.matches(".*[a-z].*")) {
            return " DEBE CONTENER AL MENOS UNA MINUSCULA";
        }

        if (!password.matches(".*\\d.*")) {
            return " DEBE CONTENER AL MENOS UN NUMERO";
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            return " DEBE CONTENER AL MENOS UN SIMBOLO (@#$%^&+=!)";
        }

        return "OK";
    }

    public String register(UserRegisterDTO user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("EMAIL " + user.getEmail() + " YA EN USO");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("USERNAME " + user.getUsername() + " YA EN USO");
        }

        String result = validatePassword(user.getPassword());
        if (!result.equals("OK")) {
            throw new PasswordFormatException("CONTRASENA " + result);
        }
        if(userRepository.count()==0){
            AdminUser ua =(AdminUser) adminUserFactory.createUser(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPassword(), user.getPhone(), Role.ADMIN);
            ua.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(ua);
            return "USUARIO REGISTRADO CORRECTAMENTE";
        }
        
        User u = (User) defaultUserFactory.createUser(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getPhone(), Role.USER);
        u.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(u);

        return "USUARIO REGISTRADO CORRECTAMENTE";
    }

    public LoginResponseDTO login(UserLoginDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("USUARIO CON EMAIL " + request.getEmail() + " NO ENCONTRADO"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("CONTRASENA INCORRECTA");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDTO(token);
    }

    public boolean verifyCode (String twilioCode, String userCode){
        return twilioCode.equals(userCode);
    }

    public String resetPassword (String email, String password){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        String result = validatePassword(password);
        if (!result.equals("OK")) {
            throw new PasswordFormatException("CONTRASENA " + result);
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "CONTRASENA CAMBIADA EXITOSAMENTE";
    }
}
