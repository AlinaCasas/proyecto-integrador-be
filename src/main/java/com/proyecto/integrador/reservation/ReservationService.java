package com.proyecto.integrador.reservation;

import com.proyecto.integrador.exceptions.BadRequestException;
import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.product.ProductRepository;
import com.proyecto.integrador.product.ProductService;
import com.proyecto.integrador.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<ReservationDTO> findReservations(){
        return reservationRepository.findAllReservationsBy();
    }

    public ReservationResponse createReservation(ReservationDTO reservation, Principal connectedUser) {

        return null;
    }

    public ReservationResponse updateReservation(Long id, UpdateReservationDTO updateReservationDTO) {

        Integer updateUserId = updateReservationDTO.getUserId();
        Long updateProductId = updateReservationDTO.getProductId();
        Long updateStartDate = updateReservationDTO.getStartDate();
        Long updateEndDate = updateReservationDTO.getEndDate();
        boolean isConfirm = updateReservationDTO.isConfirm();

        if (updateUserId == null && updateProductId == null && updateStartDate == null && updateEndDate == null ) throw new BadRequestException("No data to update");

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new BadRequestException("Reservation not found"));

        if (reservation.getDeletedAt() != null) throw new BadRequestException("The reservation is deleted, please contact support.");

        long now = new Date().getTime();

        if (reservation.getStartDate().getTime() < now) throw new BadRequestException("The reservation is already started, you can't update it");

//        TODO: Only Admins can update the user and product of a reservation
//        if (userId != null) reservation.setUser(updateReservationDTO.getUserId());
//        if (productId != null) reservation.setProduct(updateReservationDTO.getProductId());
        if (updateStartDate != null) reservation.setStartDate(new Date(updateStartDate));
        if (updateEndDate != null) reservation.setEndDate(new Date(updateEndDate));


        long startDate = reservation.getStartDate().getTime();
        long endDate = reservation.getEndDate().getTime();

        if (startDate <= now) throw new BadRequestException("Start date of reservation is invalid");
        if (endDate <= now || endDate <= startDate) throw new BadRequestException("End date of reservation is invalid");

        List<Reservation> reservationList = reservationRepository.findAllActiveReservationsByProductId(updateProductId);
        reservationList.forEach(res -> {
                long reservationStartDate = res.getStartDate().getTime();
                long reservationEndDate = res.getEndDate().getTime();
                if (startDate >= reservationStartDate && startDate <= reservationEndDate) throw new BadRequestException("The product is already reserved in that date");
                if (endDate >= reservationStartDate && endDate <= reservationEndDate) throw new BadRequestException("The product is already reserved in that date");
            }
        );

        // Calculate new price of reservation, go to product and get price and discount, calculate price with dates and discount
        Product product = productRepository.findById(updateProductId).orElseThrow(() -> new BadRequestException("Product to reserve not found"));
        float price = product.getPrice();
        int discount = product.getDiscount();

        // calculate price with discount
        float productPriceWithDiscount = price - ((price * discount) / 100);

        if (!isConfirm && reservation.getProductPrice() != productPriceWithDiscount) {
            return new ReservationResponse(false, true, "The product price to reserve changed, please confirm the reservation");
        }

        long days = (endDate - startDate) / (1000 * 60 * 60 * 24);
        float newTotalPrice = (price * days) - ((price * days) * discount / 100);
        reservation.setTotalPrice(newTotalPrice);
        reservation.setProductPrice(productPriceWithDiscount);

        Reservation saveReservation = reservationRepository.save(reservation);
        return new ReservationResponse(true, false, "Reservation updated successfully");
    }

    public void deleteReservation(Long id){
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new BadRequestException("Reservation not found"));
        reservationRepository.deleteById(id);
    }
}
