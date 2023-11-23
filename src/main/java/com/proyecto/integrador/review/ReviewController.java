package com.proyecto.integrador.review;

import com.proyecto.integrador.review.dto.CreateReviewDTO;
import com.proyecto.integrador.review.dto.ResponseReviewDTO;
import com.proyecto.integrador.review.dto.UpdateReviewDTO;
import com.proyecto.integrador.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @GetMapping()
    public ResponseEntity<List<ResponseReviewDTO>> find(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(service.findReviews(user));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ResponseReviewDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllReviews());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<ResponseReviewDTO>> findAllByProduct(@PathVariable(value = "id", required = true) Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllReviewsByProduct(id));
    }

    @PostMapping
    public ResponseEntity<ResponseReviewDTO> create(@RequestBody @Valid CreateReviewDTO review, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReview(review, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseReviewDTO> update(@PathVariable(value = "id", required = false) Long id, @RequestBody @Valid UpdateReviewDTO review, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(service.updateReview(id, review, user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        service.deleteReview(id, user);
    }
}

