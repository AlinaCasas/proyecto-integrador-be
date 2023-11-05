package com.proyecto.integrador.config.mail.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailRest {
    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("notasprestadas@gmail.com");
            message.setTo(emailRequest.getTo());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getContent());

            mailSender.send(message);

            return new ResponseEntity<>("Email enviado con exito para: "+emailRequest.getTo(), HttpStatus.OK);
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir al enviar el correo
            return new ResponseEntity<>("Fallo el envio del mail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}