package com.proyecto.integrador.config.mail.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailRest {
    private final EmailPort emailPort;

    public EmailRest(EmailPort emailPort) {
        this.emailPort = emailPort;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailBody emailBody) {
        boolean sent = emailPort.sendEmail(emailBody);
        if (sent) {
            return new ResponseEntity<>("Email enviado correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error al enviar el email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}