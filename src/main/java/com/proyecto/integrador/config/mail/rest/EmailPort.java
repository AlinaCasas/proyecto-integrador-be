package com.proyecto.integrador.config.mail.rest;

public interface EmailPort {
    boolean sendEmail(EmailBody emailBody);
}