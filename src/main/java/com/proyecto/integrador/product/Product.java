package com.proyecto.integrador.product;

import com.proyecto.integrador.auditing.Auditable;
import com.proyecto.integrador.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product extends Auditable {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true)
  @NotEmpty(message = "Instrument name is required, foe example: Guitar")
  @Size(min = 3, max = 50, message = "Instrument name should be between 3 and 50 characters")
  private String name;

  @Column(nullable = false)
  @NotEmpty(message = "Category is required")
  private String category;

  @Column(nullable = true)
  @Size(min = 3, max = 50, message = "Brand should be between 3 and 50 characters")
  private String brand;

  @Column(nullable = true)
  @Size(min = 3, max = 50, message = "Model should be between 3 and 50 characters")
  private String model;

  @Column(nullable = true)
  @Size(min = 3, max = 255, message = "Description should be between 3 and 255 characters")
  private String description;

  @Column(nullable = false)
  @NotNull(message = "Price is required")
  @Positive(message = "Price should be greater than 0")
  private Float price;

  @Column(nullable = true)
  @Size.List({
    @Size(max = 5, message = "Maximum 5 images are allowed")
  })
  private List<String> images;

  @Column(nullable = true)
  @Min(value = 0, message = "Discount should be greater than 0%")
  @Max(value = 100, message = "Discount should be less than 100%")
  private Integer discount;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Reservation> reservations;

}
