package com.proyecto.integrador.reservation;

import com.proyecto.integrador.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<ReservationDTO> findAllReservationsBy();

  List<Reservation> findAllReservationsByUserId(Long userId);

  @Query("SELECT r FROM Reservation r WHERE r.product.id = ?1 AND r.deletedAt IS NULL")
  List<Reservation> findAllActiveReservationsByProductId(Long productId);

}
