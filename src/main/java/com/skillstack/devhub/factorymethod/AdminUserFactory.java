package com.skillstack.devhub.factorymethod;

import com.skillstack.devhub.model.AbstractUser;
import com.skillstack.devhub.model.AdminUser;
import com.skillstack.devhub.model.Role;

public class AdminUserFactory implements UserFactory{

    @Override
    public AbstractUser createUser(String firstName, String lastName, String username, String email, String password, String phone, Role role) {
        return new AdminUser(firstName, lastName, username, email, password, phone, role);
    }
}
