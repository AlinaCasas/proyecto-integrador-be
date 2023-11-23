package com.proyecto.integrador.review;

import com.proyecto.integrador.review.dto.CreateReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  List<CreateReviewDTO> findAllReviewsBy();

  @Query("SELECT r FROM Review r WHERE r.deletedAt IS NULL")
  List<Review> findAllActiveReviews();

  @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.deletedAt IS NULL")
  List<Review> findAllReviewsByProductId(@Param("productId") Long productId);

  @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.deletedAt IS NULL")
  List<Review> findAllReviewsByUserId(@Param("userId") Integer userId);

  @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.product.id = :productId AND r.deletedAt IS NULL")
  Review findReviewByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Long productId);

}
