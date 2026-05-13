package com.skillstack.devhub.factorymethod;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.model.AbstractUser;
import com.skillstack.devhub.model.AdminUser;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class UserFactoryTest {

    @Test
    @CasoPrueba(
            id = "CP60",
            descripcion = "AdminUserFactory - crea un AdminUser con los campos dados",
            entrada = "firstName=Admin, lastName=User, username=adminUser, email=admin@test.com, password=Pass1!, phone=123, role=ADMIN",
            tipo = "Normal",
            esperado = "Retorna instancia de AdminUser con los datos correctos"
    )
    void adminUserFactory_createsAdminUser() {
        AdminUserFactory factory = new AdminUserFactory();
        AbstractUser user = factory.createUser(
                "Admin", "User", "adminUser",
                "admin@test.com", "Pass1!", "123", Role.ADMIN
        );

        assertNotNull(user);
        assertInstanceOf(AdminUser.class, user);
        assertEquals("Admin", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("adminUser", user.getUsername());
        assertEquals("admin@test.com", user.getEmail());
        assertEquals("Pass1!", user.getPassword());
        assertEquals("123", user.getPhone());
        assertEquals(Role.ADMIN, user.getRole());

        System.out.println("CP60 AdminUserFactory creo usuario correctamente:");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
    }

    @Test
    @CasoPrueba(
            id = "CP61",
            descripcion = "DefaultUserFactory - crea un User con el rol USER",
            entrada = "firstName=Regular, lastName=User, username=regularUser, email=user@test.com, password=Pass1!, phone=456, role=USER",
            tipo = "Normal",
            esperado = "Retorna instancia de User con los datos correctos"
    )
    void defaultUserFactory_createsUser() {
        DefaultUserFactory factory = new DefaultUserFactory();
        AbstractUser user = factory.createUser(
                "Regular", "User", "regularUser",
                "user@test.com", "Pass1!", "456", Role.USER
        );

        assertNotNull(user);
        assertInstanceOf(User.class, user);
        assertEquals("Regular", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("regularUser", user.getUsername());
        assertEquals("user@test.com", user.getEmail());
        assertEquals("Pass1!", user.getPassword());
        assertEquals("456", user.getPhone());
        assertEquals(Role.USER, user.getRole());

        System.out.println("CP61 DefaultUserFactory creo usuario correctamente:");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
    }

    @Test
    @CasoPrueba(
            id = "CP60B",
            descripcion = "AdminUserFactory - el usuario creado no es instancia de la clase base User",
            entrada = "role=ADMIN",
            tipo = "Normal",
            esperado = "El AdminUser es distinto de User regular"
    )
    void adminUserFactory_notInstanceOfDefaultUser() {
        AdminUserFactory factory = new AdminUserFactory();
        AbstractUser adminUser = factory.createUser(
                "Admin", "User", "adminUser",
                "admin@test.com", "Pass1!", "123", Role.ADMIN
        );

        DefaultUserFactory defaultFactory = new DefaultUserFactory();
        AbstractUser regularUser = defaultFactory.createUser(
                "Regular", "User", "regularUser",
                "user@test.com", "Pass1!", "456", Role.USER
        );

        assertNotEquals(adminUser.getClass(), regularUser.getClass());
        assertInstanceOf(AdminUser.class, adminUser);
        assertInstanceOf(User.class, regularUser);
    }
}
