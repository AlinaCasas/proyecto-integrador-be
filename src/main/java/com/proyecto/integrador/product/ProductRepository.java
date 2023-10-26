package com.proyecto.integrador.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

  List<ProductDTO> findAllProductsBy();

}
