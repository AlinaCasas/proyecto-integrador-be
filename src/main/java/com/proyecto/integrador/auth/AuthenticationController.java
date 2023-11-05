package com.proyecto.integrador.auth;

import com.proyecto.integrador.config.mail.rest.EmailRequest;
import com.proyecto.integrador.config.mail.rest.EmailRest;
import com.proyecto.integrador.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final EmailRest emailRest;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody @Valid RegisterRequest request
  ) {

    String subject = "¡Bienvenido a Notas Prestadas!";
    String content = "¡Hola "+request.getFirstname()+"!\n\n" +
            "En nombre de todo el equipo de Notas Prestadas, te damos la más cordial bienvenida a nuestra plataforma de alquiler de instrumentos musicales. Estamos emocionados de que te unas a nuestra comunidad de amantes de la música.\n\n" +
            "En Notas Prestadas, podrás alquilar una amplia gama de instrumentos de alta calidad, desde guitarras y teclados hasta tambores y más. Nuestra misión es facilitar tu viaje musical y ayudarte a encontrar el instrumento perfecto para expresar tu creatividad.\n\n" +
            "¿Qué puedes hacer ahora?\n" +
            "1. Explora nuestra selección de instrumentos y encuentra el que se adapte a tu estilo.\n" +
            "2. Configura tu cuenta y comienza a alquilar en cuestión de minutos.\n" +
            "3. No dudes en contactarnos si tienes alguna pregunta o necesitas ayuda. Estamos aquí para apoyarte.\n\n" +
            "¡Esperamos que disfrutes al máximo tu experiencia en Notas Prestadas! ¡Que la música nunca deje de sonar en tu vida!\n\n" +
            "Saludos musicales,\n" +
            "El equipo de Notas Prestadas\n"+
            "notasprestadas@gmail.com";

    EmailRequest emailRequest = new EmailRequest(request.getEmail(), subject, content);
    emailRest.sendEmail(emailRequest);

    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody @Valid AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.login(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}
