package com.skillstack.devhub.factorymethod;

import com.skillstack.devhub.model.AbstractUser;
import com.skillstack.devhub.model.Role;

public interface UserFactory {
    AbstractUser createUser (String firstName, String lastName, String username, String email, String password, String phone, Role role);
}
