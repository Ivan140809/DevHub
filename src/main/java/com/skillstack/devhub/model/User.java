package com.skillstack.devhub.model;

import com.skillstack.devhub.Observer.Observer;
import com.skillstack.devhub.service.EmailSenderService;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public class User extends AbstractUser implements Observer {
    @Id
    private String id;

    // transient: no se persiste en MongoDB, se inyecta desde el service
    @Transient
    private transient EmailSenderService emailSenderService;

    public User() {
    }

    public User(String firstName, String lastName, String username, String email, String password, String phone, Role role, int totalScore) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.phone = phone;
        this.totalScore = totalScore;
    }

    public void setEmailSenderService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void update(String message, Comment comment) {
        System.out.println("[NOTIFICACION para " + username + "] "
                + message
                + " | Contenido: \"" + comment.getContent() + "\"");

        if (emailSenderService != null) {
            emailSenderService.sendEmail(
                    email,
                    "DevHub - Nueva notificación en un comentario",
                    "Hola " + username + ",\n\n"
                            + message + "\n\n"
                            + "Comentario: \"" + comment.getContent() + "\"\n\n"
                            + "Ingresa a DevHub para verlo."
            );
        }
    }

    public String getId() {
        return id;
    }

}
