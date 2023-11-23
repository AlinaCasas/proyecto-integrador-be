package com.proyecto.integrador.review;

import com.proyecto.integrador.auditing.Auditable;
import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false, referencedColumnName = "id")
    private Product product;

    @Column(nullable = false)
    @NotNull(message = "Comment is required")
    @Size(min = 3, max = 255, message = "Comment should be between 3 and 255 characters")
    private String comment;

    @Column(nullable = false)
    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating should be greater than 0")
    @Max(value = 5, message = "Rating should be less than 5")
    private Float rating;
}
