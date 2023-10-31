package com.proyecto.integrador.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class ResponseReservationDTO {
    private Long id;
    private Integer userId;
    private Long productId;
    private Date startDate;
    private Date endDate;
    private Float productPrice;
    private Float totalPrice;
    private Date createdAt;
    private Date updatedAt;
}
