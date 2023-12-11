package com.proyecto.integrador.reservation.dto;

import com.proyecto.integrador.product.dto.ProductDTO;
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
    private Date startDate;
    private Date endDate;
    private Float productPrice;
    private Float totalPrice;
    private Date createdAt;
    private Date updatedAt;
    private ProductDTO product;
}
