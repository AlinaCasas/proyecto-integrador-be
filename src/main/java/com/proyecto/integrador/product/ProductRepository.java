package com.proyecto.integrador.product;

import com.proyecto.integrador.product.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
  List<ProductDTO> findAllProductsBy();

  Product findByName(String name);

  @Query(value = "SELECT * FROM product p " +
          "WHERE p.deleted_at IS NULL " +
          "AND p.name LIKE CONCAT('%', :query, '%') ORDER BY RAND() LIMIT 5",
          nativeQuery = true)
  List<Product> findSuggestionsProducts(@Param("query") String query);

    @Query(value = "SELECT p.name FROM product p " +
            "WHERE p.deleted_at IS NULL " +
            "AND p.name LIKE CONCAT('%', :query, '%') ORDER BY p.name ASC LIMIT 10",
            nativeQuery = true)
    List<String> findSuggestionsNames(@Param("query") String query);


  @Query(value = "SELECT * FROM product p " +
          "WHERE p.deleted_at IS NULL " +
          "AND (:id IS NULL OR p.id = :id) " +
          "AND (:name IS NULL OR p.name LIKE CONCAT('%', :name, '%')) " +
          "AND (:category IS NULL OR p.category LIKE CONCAT('%', :category, '%')) " +
          "AND (:brand IS NULL OR p.brand LIKE CONCAT('%', :brand, '%')) " +
          "AND (:model IS NULL OR p.model LIKE CONCAT('%', :model, '%')) " +
          "AND (:description IS NULL OR p.description LIKE CONCAT('%', :description, '%')) " +
          "AND (:priceMin IS NULL OR p.price >= :priceMin) " +
          "AND (:priceMax IS NULL OR p.price <= :priceMax) " +
          "AND (:discount IS NULL OR p.discount >= :discount) " +
          "AND (:startDate IS NULL OR NOT EXISTS ( " +
          "SELECT 1 FROM reservations r WHERE r.product_id = p.id AND r.deleted_at IS NULL " +
          "AND ((r.start_date BETWEEN :startDate AND :endDate) OR (r.end_date BETWEEN :startDate AND :endDate)))) " +
          "ORDER BY " +
          "CASE " +
            "WHEN :order = 'random' THEN RAND() " +
            "WHEN :order = 'asc' AND :sort = 'id' THEN p.id " +
            "WHEN :order = 'asc' AND :sort = 'name' THEN p.name " +
            "WHEN :order = 'asc' AND :sort = 'category' THEN p.category " +
            "WHEN :order = 'asc' AND :sort = 'brand' THEN p.brand " +
            "WHEN :order = 'asc' AND :sort = 'model' THEN p.model " +
            "WHEN :order = 'asc' AND :sort = 'description' THEN p.description " +
            "WHEN :order = 'asc' AND :sort = 'price' THEN p.price " +
            "WHEN :order = 'asc' AND :sort = 'discount' THEN p.discount " +
          "END ASC, " +
          "CASE " +
            "WHEN :order = 'desc' AND :sort = 'id' THEN p.id " +
            "WHEN :order = 'desc' AND :sort = 'name' THEN p.name " +
            "WHEN :order = 'desc' AND :sort = 'category' THEN p.category " +
            "WHEN :order = 'desc' AND :sort = 'brand' THEN p.brand " +
            "WHEN :order = 'desc' AND :sort = 'model' THEN p.model " +
            "WHEN :order = 'desc' AND :sort = 'description' THEN p.description " +
            "WHEN :order = 'desc' AND :sort = 'price' THEN p.price " +
            "WHEN :order = 'desc' AND :sort = 'discount' THEN p.discount " +
          "END DESC ",
          countQuery = "SELECT * FROM product p " +
          "WHERE p.deleted_at IS NULL " +
          "AND (:id IS NULL OR p.id = :id) " +
          "AND (:name IS NULL OR p.name LIKE CONCAT('%', :name, '%')) " +
          "AND (:category IS NULL OR p.category LIKE CONCAT('%', :category, '%')) " +
          "AND (:brand IS NULL OR p.brand LIKE CONCAT('%', :brand, '%')) " +
          "AND (:model IS NULL OR p.model LIKE CONCAT('%', :model, '%')) " +
          "AND (:description IS NULL OR p.description LIKE CONCAT('%', :description, '%')) " +
          "AND (:priceMin IS NULL OR p.price >= :priceMin) " +
          "AND (:priceMax IS NULL OR p.price <= :priceMax) " +
          "AND (:discount IS NULL OR p.discount >= :discount) " +
          "AND (:startDate IS NULL OR NOT EXISTS ( " +
          "SELECT 1 FROM reservations r WHERE r.product_id = p.id AND r.deleted_at IS NULL " +
          "AND ((r.start_date BETWEEN :startDate AND :endDate) OR (r.end_date BETWEEN :startDate AND :endDate)))) ",
          nativeQuery = true)
  Page<Product> findAllWithFilters(@Param("id") Long id,
                                   @Param("name") String name,
                                   @Param("category") String category,
                                   @Param("brand") String brand,
                                   @Param("model") String model,
                                   @Param("description") String description,
                                   @Param("priceMin") Float priceMin,
                                   @Param("priceMax") Float priceMax,
                                   @Param("discount") Integer discount,
                                   @Param("startDate") Date startDate,
                                   @Param("endDate") Date endDate,
                                   @Param("sort") String sort,
                                   @Param("order") String order,
                                   Pageable pageable);



}

