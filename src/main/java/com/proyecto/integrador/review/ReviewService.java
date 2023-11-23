package com.proyecto.integrador.review;

import com.proyecto.integrador.exceptions.BadRequestException;
import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.product.ProductRepository;
import com.proyecto.integrador.reservation.ReservationRepository;
import com.proyecto.integrador.review.dto.CreateReviewDTO;
import com.proyecto.integrador.review.dto.ResponseReviewDTO;
import com.proyecto.integrador.review.dto.UpdateReviewDTO;
import com.proyecto.integrador.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private ResponseReviewDTO reviewToReviewDTO(Review review) {
        return ResponseReviewDTO.builder()
                .id(review.getId())
                .userName(review.getUser().getFirstname() + " " + review.getUser().getLastname())
                .productId(review.getProduct().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
    public List<ResponseReviewDTO> findReviews(User user) {
        List<Review> reviewList = reviewRepository.findAllReviewsByUserId(user.getId());
        return reviewList.stream().map(this::reviewToReviewDTO).toList();
    }

    public List<ResponseReviewDTO> findAllReviews() {
        List<Review> reviewList = reviewRepository.findAllActiveReviews();
        return reviewList.stream().map(this::reviewToReviewDTO).toList();
    }

    public List<ResponseReviewDTO> findAllReviewsByProduct(Long id) {
        List<Review> reviewList = reviewRepository.findAllReviewsByProductId(id);
        return reviewList.stream().map(this::reviewToReviewDTO).toList();
    }

    public ResponseReviewDTO createReview(CreateReviewDTO reservationDTO, User user) {
        if (reservationRepository.findReservationByUserIdAndProductId(user.getId(), reservationDTO.getProductId()) == null) {
            throw new BadRequestException("You don't have a reservation for this product");
        }

        if (reviewRepository.findReviewByUserIdAndProductId(user.getId(), reservationDTO.getProductId()) != null) {
            throw new BadRequestException("You have already reviewed this product");
        }

        Product product = productRepository.findById(reservationDTO.getProductId()).orElseThrow(() -> new BadRequestException("Product to reserve not found"));

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(reservationDTO.getRating())
                .comment(reservationDTO.getComment())
                .build();

        Review saveReview = reviewRepository.save(review);

        Float currentRating = product.getRating();
        if (currentRating == null) currentRating = 0f;
        Integer currentRatingCount = product.getRatingCount();
        if (currentRatingCount == null) currentRatingCount = 0;

        Float newRating = (currentRating * currentRatingCount + reservationDTO.getRating()) / (currentRatingCount + 1);

        product.setRating(newRating);
        product.setRatingCount(currentRatingCount + 1);
        productRepository.save(product);

        return reviewToReviewDTO(saveReview);
    }

    public ResponseReviewDTO updateReview(Long id, UpdateReviewDTO updateReviewDTO, User user) {
        String comment = updateReviewDTO.getComment();
        Float rating = updateReviewDTO.getRating();
        if (comment == null && rating == null) throw new BadRequestException("You must provide at least one field to update");

        Review review = reviewRepository.findById(id).orElseThrow(() -> new BadRequestException("Review not found"));

        if (!Objects.equals(review.getUser().getId(), user.getId())) throw new BadRequestException("You can't update this review");

        if (comment != null && !comment.isEmpty()) review.setComment(comment);

        if (rating != null && rating > 0) {
            Product product = review.getProduct();
            Float currentRating = product.getRating();
            if (currentRating == null) currentRating = 0f;
            Integer currentRatingCount = product.getRatingCount();
            if (currentRatingCount == null) currentRatingCount = 0;
            Float newRating = (currentRating * currentRatingCount - review.getRating() + rating) / currentRatingCount;
            product.setRating(newRating);
            review.setProduct(product);
            review.setRating(rating);
        }

        Review saveReview = reviewRepository.save(review);
        productRepository.save(review.getProduct());
        return reviewToReviewDTO(saveReview);
    }

    public void deleteReview(Long id, User user) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new BadRequestException("Reservation not found"));
        if (!Objects.equals(review.getUser().getId(), user.getId())) throw new BadRequestException("You can't delete this reservation");
        review.setDeletedAt(new Date());
        reviewRepository.save(review);

        Product product = review.getProduct();
        Float currentRating = product.getRating();
        if (currentRating == null) currentRating = 0f;
        Integer currentRatingCount = product.getRatingCount();
        if (currentRatingCount == null) currentRatingCount = 0;
        Float newRating = (currentRating * currentRatingCount - review.getRating()) / (currentRatingCount - 1);
        product.setRating(newRating);
        product.setRatingCount(currentRatingCount - 1);
        productRepository.save(product);
    }
}
