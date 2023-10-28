package com.proyecto.integrador.reservation;

import com.proyecto.integrador.auditing.Auditable;
import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Getter
@Data
@Entity
@Table(name = "reservations")
public class Reservation extends Auditable {
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
    private Float productPrice;

    @Column(nullable = false)
    private Float totalPrice;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

}
