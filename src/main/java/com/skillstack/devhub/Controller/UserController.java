package com.skillstack.devhub.Controller;



import com.skillstack.devhub.Model.User;
import com.skillstack.devhub.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registro")
    public String registerUser(@RequestBody User user) {
        userService.register(user);
        return "Usuario registrado correctamente";
    }
}