package com.proyecto.integrador.favorites;

import com.proyecto.integrador.auditing.Auditable;
import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "user_favorite_product")
public class UserFavoriteProduct extends Auditable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getter y Setter para el campo product
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
