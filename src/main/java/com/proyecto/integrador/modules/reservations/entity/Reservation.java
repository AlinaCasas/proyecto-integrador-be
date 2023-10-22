package com.proyecto.integrador.modules.reservations.entity;

import com.proyecto.integrador.common.Auditable;
import com.proyecto.integrador.modules.products.entity.Product;
import com.proyecto.integrador.modules.users.entity.User;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column()
    private String id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false, referencedColumnName = "id")
    private Product product;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

}
