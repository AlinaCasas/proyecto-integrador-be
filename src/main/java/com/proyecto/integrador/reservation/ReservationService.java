package com.proyecto.integrador.reservation;

import com.proyecto.integrador.exceptions.BadRequestException;
import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.product.ProductRepository;
import com.proyecto.integrador.reservation.dto.CreateReservationDTO;
import com.proyecto.integrador.reservation.dto.ResponseReservationDTO;
import com.proyecto.integrador.reservation.dto.UpdateReservationDTO;
import com.proyecto.integrador.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ProductRepository productRepository;

    private ResponseReservationDTO reservationToReservationDTO(Reservation reservation) {
        return ResponseReservationDTO.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .productId(reservation.getProduct().getId())
                .productPrice(reservation.getProductPrice())
                .totalPrice(reservation.getTotalPrice())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }
    public List<ResponseReservationDTO> findReservations(User user) {
        List<Reservation> reservationList = reservationRepository.findAllReservationsByUserId(user.getId());
        return reservationList.stream().map(this::reservationToReservationDTO).toList();
    }

    public List<ResponseReservationDTO> findAllReservations() {
        List<Reservation> reservationList = reservationRepository.findAllActiveReservations();
        return reservationList.stream().map(this::reservationToReservationDTO).toList();
    }

    public ReservationResponse createReservation(CreateReservationDTO reservationDTO, User user) {
        Long productId = reservationDTO.getProductId();
        long startDate = reservationDTO.getStartDate() * 1000;
        long endDate = reservationDTO.getEndDate() * 1000;

        long now = new Date().getTime();

        if (startDate < now) throw new BadRequestException("Start date of reservation is invalid");
        if (endDate < now || endDate < startDate) throw new BadRequestException("End date of reservation is invalid");

        Product product = productRepository.findById(productId).orElseThrow(() -> new BadRequestException("Product to reserve not found"));

        List<Reservation> reservationList = reservationRepository.findAllActiveReservationsByProductId(productId);

        reservationList.forEach(res -> {
                long reservationStartDate = res.getStartDate().getTime();
                long reservationEndDate = res.getEndDate().getTime();
                if (startDate >= reservationStartDate && startDate <= reservationEndDate) throw new BadRequestException("The product is already reserved in that date");
                if (endDate >= reservationStartDate && endDate <= reservationEndDate) throw new BadRequestException("The product is already reserved in that date");
            }
        );

        float price = product.getPrice();
        int discount = product.getDiscount();
        float productPriceWithDiscount = price - ((price * discount) / 100);

        long days = (endDate - startDate) / (1000 * 60 * 60 * 24);
        float newTotalPrice = (price * days) - ((price * days) * discount / 100);

        Reservation reservation = Reservation.builder()
                .user(user)
                .product(product)
                .startDate(new Date(startDate))
                .endDate(new Date(endDate))
                .totalPrice(newTotalPrice)
                .productPrice(productPriceWithDiscount)
                .build();

        Reservation saveReservation = reservationRepository.save(reservation);

       return new ReservationResponse("Reservation confirmed successfully", true, false);
    }

    public ReservationResponse updateReservation(Long id, UpdateReservationDTO updateReservationDTO, User user) {

        Long updateProductId = updateReservationDTO.getProductId();
        Long updateStartDate = updateReservationDTO.getStartDate() != null ? updateReservationDTO.getStartDate() * 1000 : null;
        Long updateEndDate = updateReservationDTO.getEndDate() != null ? updateReservationDTO.getEndDate() * 1000 : null;
        boolean isConfirm = updateReservationDTO.isConfirm();

        if (updateProductId == null && updateStartDate == null && updateEndDate == null ) throw new BadRequestException("No data to update");

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new BadRequestException("Reservation not found"));

        if (!Objects.equals(reservation.getUser().getId(), user.getId())) throw new BadRequestException("You can't update this reservation");

        if (reservation.getDeletedAt() != null) throw new BadRequestException("The reservation is deleted, please contact support.");

        long now = new Date().getTime();

        if (reservation.getStartDate().getTime() < now) throw new BadRequestException("The reservation is already started, you can't update it");

        Long productId = updateProductId != null ? updateProductId : reservation.getProduct().getId();

        if (updateStartDate != null) reservation.setStartDate(new Date(updateStartDate));
        if (updateEndDate != null) reservation.setEndDate(new Date(updateEndDate));

        long startDate = reservation.getStartDate().getTime();
        long endDate = reservation.getEndDate().getTime();

        if (startDate <= now) throw new BadRequestException("Start date of reservation is invalid");
        if (endDate <= now || endDate <= startDate) throw new BadRequestException("End date of reservation is invalid");

        Product product = productRepository.findById(productId).orElseThrow(() -> new BadRequestException("Product to reserve not found"));

        List<Reservation> reservationList = reservationRepository.findAllActiveReservationsByProductId(productId);
        reservationList.forEach(res -> {
                long reservationStartDate = res.getStartDate().getTime();
                long reservationEndDate = res.getEndDate().getTime();
                if (startDate >= reservationStartDate && startDate <= reservationEndDate) throw new BadRequestException("The product is already reserved in that date");
                if (endDate >= reservationStartDate && endDate <= reservationEndDate) throw new BadRequestException("The product is already reserved in that date");
            }
        );

        // Calculate new price of reservation, go to product and get price and discount, calculate price with dates and discount
        float price = product.getPrice();
        int discount = product.getDiscount();

        // calculate price with discount
        float productPriceWithDiscount = price - ((price * discount) / 100);

        if (!isConfirm && reservation.getProductPrice() != productPriceWithDiscount) {
            return new ReservationResponse("The product price to reserve changed, please confirm the reservation", false, true);
        }

        long days = (endDate - startDate) / (1000 * 60 * 60 * 24);
        float newTotalPrice = (price * days) - ((price * days) * discount / 100);
        reservation.setTotalPrice(newTotalPrice);
        reservation.setProductPrice(productPriceWithDiscount);
        reservation.setProduct(product);

        Reservation saveReservation = reservationRepository.save(reservation);
        return new ReservationResponse("Reservation updated successfully", true, false);
    }

    public void deleteReservation(Long id, User user) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new BadRequestException("Reservation not found"));
        if (!Objects.equals(reservation.getUser().getId(), user.getId())) throw new BadRequestException("You can't delete this reservation");
        reservation.setDeletedAt(new Date());
        reservationRepository.save(reservation);
    }

    public String findProductNameById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        return productOptional.map(Product::getName).orElse(null);
    }
}
