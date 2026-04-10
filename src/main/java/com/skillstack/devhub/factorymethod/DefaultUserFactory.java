package com.skillstack.devhub.factorymethod;

import com.skillstack.devhub.model.AbstractUser;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserFactory implements UserFactory{
    @Override
    public AbstractUser createUser(String firstName, String lastName, String username, String email, String password, String phone, Role role, int totalScore) {
        return new User(firstName, lastName, username, email, password, phone, role, totalScore);
    }
}
