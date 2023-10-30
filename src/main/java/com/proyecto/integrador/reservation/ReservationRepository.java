package com.proyecto.integrador.reservation;

import com.proyecto.integrador.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<ReservationDTO> findAllReservationsBy();

  List<Reservation> findAllReservationsByUserId(Long userId);

  @Query("SELECT r FROM Reservation r WHERE r.product.id = :productId AND r.deletedAt IS NULL")
  List<Reservation> findAllActiveReservationsByProductId(@Param("productId") Long productId);

}
