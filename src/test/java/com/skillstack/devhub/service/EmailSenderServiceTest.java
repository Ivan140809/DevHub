package com.skillstack.devhub.service;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailSenderService emailSenderService;

    @Test
    @CasoPrueba(
            id = "CP79",
            descripcion = "EmailSenderService - sendEmail invoca mailSender con los datos correctos",
            entrada = "toEmail=dest@test.com, subject=Asunto, body=Cuerpo del mensaje",
            tipo = "Normal",
            esperado = "Se llama a mailSender.send con los campos from, to, subject y text correctos"
    )
    void sendEmail_callsMailSenderWithCorrectParams() {
        String toEmail = "dest@test.com";
        String subject = "Asunto";
        String body = "Cuerpo del mensaje";

        emailSenderService.sendEmail(toEmail, subject, body);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertNotNull(message.getTo());
        assertEquals(1, message.getTo().length);
        assertEquals(toEmail, message.getTo()[0]);
        assertEquals(subject, message.getSubject());
        assertEquals(body, message.getText());
        assertEquals("devhubfis@gmail.com", message.getFrom());

        System.out.println("CP79 Email enviado correctamente a: " + toEmail);
        System.out.println("Asunto: " + subject);
    }

    @Test
    @CasoPrueba(
            id = "CP79B",
            descripcion = "EmailSenderService - sendEmail llama a mailSender exactamente una vez",
            entrada = "toEmail=otro@test.com, subject=Test, body=Test body",
            tipo = "Normal",
            esperado = "mailSender.send se invoca exactamente una vez"
    )
    void sendEmail_mailSenderCalledOnce() {
        emailSenderService.sendEmail("otro@test.com", "Test", "Test body");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
