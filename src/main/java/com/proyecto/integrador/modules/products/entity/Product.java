package com.proyecto.integrador.modules.products.entity;

import com.proyecto.integrador.common.Auditable;
import com.proyecto.integrador.modules.reservations.entity.Reservation;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@Data
@Entity
@Table(name = "products")
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column()
    private String id;

    @Column(nullable = false)
    @NotEmpty(message = "Instrument name is required, foe example: Guitar")
    @Size(min = 3, max = 50, message = "Instrument name should be between 3 and 50 characters")
    private String instrument;

    @Column(nullable = false)
    @NotEmpty(message = "Category is required")
    private String category;

    @Column(nullable = false)
    @NotEmpty(message = "Brand is required, for example: Casio")
    @Size(min = 3, max = 50, message = "Brand should be between 3 and 50 characters")
    private String brand;

    @Column(nullable = true)
    @Nullable
    @Size(min = 3, max = 50, message = "Model should be between 3 and 50 characters")
    private String model;

    @Column(nullable = true)
    @Nullable
    @Size(min = 3, max = 255, message = "Description should be between 3 and 255 characters")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Price is required")
    @Positive(message = "Price should be greater than 0")
    private Float price;

    @Column(nullable = true)
    @Nullable
    private List<String> images;

    @Column(nullable = true)
    @Min(value = 0, message = "Discount should be greater than 0%")
    @Max(value = 100, message = "Discount should be less than 100%")
    @Nullable
    private Integer discount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Reservation> reservations;
}
