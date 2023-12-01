package com.proyecto.integrador.characteristics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {
    List<Characteristic> findAllByDeletedAtIsNull();
    Optional<Characteristic> findByName(String name);
}
