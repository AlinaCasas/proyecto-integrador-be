package com.proyecto.integrador.reservation;

import com.proyecto.integrador.reservation.dto.CreateReservationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<CreateReservationDTO> findAllReservationsBy();

  @Query("SELECT r FROM Reservation r WHERE r.deletedAt IS NULL")
  List<Reservation> findAllActiveReservations();

  @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.deletedAt IS NULL")
  List<Reservation> findAllReservationsByUserId(@Param("userId") Integer userId);

  @Query("SELECT r FROM Reservation r WHERE r.product.id = :productId AND r.deletedAt IS NULL")
  List<Reservation> findAllActiveReservationsByProductId(@Param("productId") Long productId);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.product.id = :productId AND r.deletedAt IS NULL")
    Reservation findReservationByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Long productId);
}
