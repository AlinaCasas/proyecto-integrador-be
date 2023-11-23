package com.proyecto.integrador.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class ResponseReviewDTO {
    private Long id;
    private String userName;
    private Long productId;
    private Float rating;
    private String comment;
    private Date createdAt;
    private Date updatedAt;
}
