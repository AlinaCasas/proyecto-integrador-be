package com.proyecto.integrador.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationResponse {
    boolean isReserved;
    boolean needConfirmation;
    String message;
}
