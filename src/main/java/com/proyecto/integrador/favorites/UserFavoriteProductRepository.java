package com.proyecto.integrador.favorites;

import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserFavoriteProductRepository extends JpaRepository<UserFavoriteProduct, Long> {

    List<UserFavoriteProduct> findByUserEmail(String userEmail);

    // Método para eliminar la relación favorita por usuario y producto
    void deleteByUserAndProduct(User user, Product product);

    // Método para verificar la relación favorita por usuario y producto
    @Query("SELECT uf FROM UserFavoriteProduct uf WHERE uf.user = :user AND uf.product = :product")
    UserFavoriteProduct findByUserAndProduct(@Param("user") User user, @Param("product") Product product);

}