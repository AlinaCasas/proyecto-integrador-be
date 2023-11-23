package com.proyecto.integrador.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByDeletedAtIsNull();

//    Category findByName(String name);

    Optional<Category> findByName(String name);

}