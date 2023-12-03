package com.proyecto.integrador.reservation;

import com.proyecto.integrador.config.mail.rest.EmailRequest;
import com.proyecto.integrador.config.mail.rest.EmailRest;
import com.proyecto.integrador.reservation.dto.CreateReservationDTO;
import com.proyecto.integrador.reservation.dto.ResponseReservationDTO;
import com.proyecto.integrador.reservation.dto.UpdateReservationDTO;
import com.proyecto.integrador.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @GetMapping()
    public ResponseEntity<List<ResponseReservationDTO>> find(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(service.findReservations(user));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ResponseReservationDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllReservations());
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@ModelAttribute @Valid CreateReservationDTO reservationDTO, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ReservationResponse response = service.createReservation(reservationDTO, user);

        // Envía un correo electrónico solo si la reserva se creó correctamente
        sendReservationConfirmationEmail(user.getEmail(), reservationDTO);


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private void sendReservationConfirmationEmail(String userEmail, CreateReservationDTO reservationDTO) {
        String subject = "Confirmación de Reserva en Notas Prestadas";
        String content = "¡Hola!\n\n" +
                "Te confirmamos que tu reserva en Notas Prestadas ha sido realizada con éxito. Aquí están los detalles de tu reserva:\n\n" +
                "Producto: [Nombre del Producto]\n" + // Reemplaza con el nombre real del producto
                "Fecha de Inicio: " + convertTimestampToDateString(reservationDTO.getStartDate()) + "\n" +
                "Fecha de Fin: " + convertTimestampToDateString(reservationDTO.getEndDate()) + "\n\n" +
                "¡Gracias por elegir Notas Prestadas para tus necesidades musicales!\n\n" +
                "Saludos,\n" +
                "El equipo de Notas Prestadas\n"+
                "notasprestadas@gmail.com";

        EmailRequest emailRequest = new EmailRequest(userEmail, subject, content);
        EmailRest emailRest = new EmailRest();
        emailRest.sendEmail(emailRequest);
    }

    // Método para convertir una marca de tiempo en formato de cadena de fecha
    private String convertTimestampToDateString(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return zonedDateTime.format(formatter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> update(@PathVariable(value = "id", required = false) Long id, @RequestBody @Valid UpdateReservationDTO reservation, Authentication authentication, Errors errors) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(service.updateReservation(id, reservation, user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        service.deleteReservation(id, user);
    }
}

