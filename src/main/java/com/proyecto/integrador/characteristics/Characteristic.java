package com.proyecto.integrador.characteristics;

import com.proyecto.integrador.auditing.Auditable;
import com.proyecto.integrador.category.Category;
import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.review.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Characteristic")
public class Characteristic extends Auditable {
    @Id
    private String name;

    @Column(name = "image", nullable = true)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category", nullable = true, referencedColumnName = "name")
    private Category category;

    @OneToMany(mappedBy = "characteristic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products;

}