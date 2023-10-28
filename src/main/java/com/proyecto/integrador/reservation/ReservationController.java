package com.proyecto.integrador.reservation;

import com.proyecto.integrador.reservation.Reservation;
import com.proyecto.integrador.reservation.ReservationDTO;
import com.proyecto.integrador.reservation.ReservationService;
import com.proyecto.integrador.reservation.UpdateReservationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ReservationResponse> create(@RequestBody(required = false) @Valid ReservationDTO reservation, Principal connectedUser) {
        String username = connectedUser.getName();
        var test = connectedUser.implies(null);
        System.out.println("reservation: " + reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReservation(reservation, connectedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> update(@PathVariable(value = "id", required = false) Long id, @RequestBody @Valid UpdateReservationDTO reservation, Errors errors) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateReservation(id, reservation));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.deleteReservation(id);
    }
}

