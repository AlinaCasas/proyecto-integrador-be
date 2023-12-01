package com.proyecto.integrador.favorites;

import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavoriteProductRepository extends JpaRepository<UserFavoriteProduct, Long> {

    List<UserFavoriteProduct> findByUserEmail(String userEmail);

    // Método para eliminar la relación favorita por usuario y producto
    void deleteByUserAndProduct(User user, Product product);

}