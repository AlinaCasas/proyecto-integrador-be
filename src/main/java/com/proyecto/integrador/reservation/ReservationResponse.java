package com.proyecto.integrador.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationResponse {
    String message;
    boolean isReserved;
    boolean needConfirmation;
}
