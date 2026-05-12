package com.skillstack.devhub.repository;

import com.skillstack.devhub.model.AbstractUser;
import com.skillstack.devhub.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<AbstractUser, String> {
    //optional sirve para q si no se encuentra, no devuelva un null, sino algo opcional
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findTop50ByOrderByTotalScoreDesc();
    boolean existsByPhone(String phone);

}