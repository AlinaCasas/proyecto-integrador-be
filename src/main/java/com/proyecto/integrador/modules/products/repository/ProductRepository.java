package com.proyecto.integrador.modules.products.repository;

import com.proyecto.integrador.modules.products.dto.ProductDTO;
import com.proyecto.integrador.modules.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT new com.proyecto.integrador.modules.products.dto.ProductDTO(p.id, p.instrument, p.category, p.brand, p.model, p.description, p.price, p.images, p.discount) FROM Product p")
    List<ProductDTO> findAllProducts();

}
