package com.skillstack.devhub.Repository;

import com.skillstack.devhub.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    //optional sirve para q si no se encuentra, no devuelva un null, sino algo opcional
    Optional<User> findByEmail(String email);
                   //findBy ---- Email (mismo nombre que en user)

    //aqui se definen los metodos para interactuar con la base de datos, como save, findAll, findById, deleteById, etc
    //tambien se pueden definir metodos personalizados, como findByEmail, findByName
    //Esto es asi porque MongoRepository ya tiene implementados los metodos basicos para interactuar con la base de datos
    // , y al extenderlo, UserRepository hereda esos metodos y puede usarlos directamente sin necesidad de implementarlos

    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}