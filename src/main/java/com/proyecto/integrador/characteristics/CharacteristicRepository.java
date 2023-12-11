package com.proyecto.integrador.characteristics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {
    List<Characteristic> findAllByDeletedAtIsNull();
    List<Characteristic> findAllByDeletedAtIsNotNull();
    Optional<Characteristic> findByName(String name);

    @Query(value = "SELECT c.* FROM characteristic c " +
            "INNER JOIN product_characteristics pc ON c.name = pc.characteristics_name " +
            "WHERE pc.products_id = :id AND c.deleted_at IS NULL",
            nativeQuery = true)
    List<Characteristic> findAllByProductsId(Long id);
}
