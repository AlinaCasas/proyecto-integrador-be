package com.proyecto.integrador.reservation;

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

import java.util.List;

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
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid CreateReservationDTO reservation, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReservation(reservation, user));
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

