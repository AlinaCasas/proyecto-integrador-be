package com.proyecto.integrador.reservation;

import com.proyecto.integrador.common.Response;
import com.proyecto.integrador.reservation.Reservation;
import com.proyecto.integrador.reservation.ReservationDTO;
import com.proyecto.integrador.reservation.ReservationService;
import com.proyecto.integrador.reservation.UpdateReservationDTO;
import com.proyecto.integrador.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> find() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findReservations());
    }

    @PostMapping
    public ResponseEntity<Response<ReservationResponse>> create(@RequestBody @Valid ReservationDTO reservation, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReservation(reservation, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<ReservationResponse>> update(@PathVariable(value = "id", required = false) Long id, @RequestBody @Valid UpdateReservationDTO reservation, Authentication authentication, Errors errors) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(service.updateReservation(id, reservation, user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.deleteReservation(id);
    }
}

