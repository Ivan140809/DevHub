package com.skillstack.devhub.Controller;



import com.skillstack.devhub.Model.User;
import com.skillstack.devhub.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@CrossOrigin
public class UserController {

    //aqui se definen los endpoints para manejar las solicitudes relacionadas con los usuarios,
    // como crear un usuario, listar usuarios, actualizar un usuario, eliminar un usuario, etc

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {

        try {
            userService.register(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Usuario registrado correctamente");

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
