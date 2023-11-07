package com.proyecto.integrador.product;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.proyecto.integrador.exceptions.BadRequestException;
import com.proyecto.integrador.product.dto.ProductDTO;
import com.proyecto.integrador.product.dto.ProductReservationDTO;
import com.proyecto.integrador.product.dto.UpdateProductDTO;
import com.proyecto.integrador.reservation.Reservation;
import com.proyecto.integrador.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AmazonS3 amazonS3;

    private ProductReservationDTO reservationToProductReservationDTO (Reservation reservation) {
        return ProductReservationDTO.builder()
                .id(reservation.getId())
                .startDate(reservation.getStartDate().getTime())
                .endDate(reservation.getEndDate().getTime())
                .build();
    }

    private ProductDTO productToProductDTO(Product product) {
        List<ProductReservationDTO> reservations = new ArrayList<>();
        if (product.getReservations() != null && !product.getReservations().isEmpty()) {
            reservations = product.getReservations().stream().map(this::reservationToProductReservationDTO).toList();
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .brand(product.getBrand())
                .model(product.getModel())
                .description(product.getDescription())
                .price(product.getPrice())
                .images(product.getImages())
                .discount(product.getDiscount())
                .reservations(reservations)
                .build();
    }

    public Page<ProductDTO> findProductsUsingSQL(Long id, String name, String category, String brand, String model, String description, Float priceMin, Float priceMax, Integer discount, int page, int limit, String sort, String order, Long startDate, Long endDate) {
        if (startDate != null && endDate == null || startDate == null && endDate != null) {
            throw new BadRequestException("If you want to filter by date, you must send both start and end date");
        }
        if (startDate != null && startDate > endDate) {
            throw new BadRequestException("The start date must be less than the end date");
        }
        page = page == 0 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(page, limit);

        Page<Product> productPage = productRepository.findAllWithFilters(
                id,
                name,
                category,
                brand,
                model,
                description,
                priceMin,
                priceMax,
                discount,
                startDate != null ? new Date(startDate * 1000) : null,
                endDate != null ? new Date(endDate * 1000) : null,
                sort,
                order,
                pageable
        );
        return productPage.map(this::productToProductDTO);
    }

    public ProductDTO findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product not found"));
        if (product.getDeletedAt() != null) throw new BadRequestException("The product is deleted, please contact support.");

        List<Reservation> reservationList = reservationRepository.findAllActiveReservationsByProductId(id);
        List<ProductReservationDTO> reservations = reservationList.stream().map(this::reservationToProductReservationDTO).toList();
        return new ProductDTO(product.getId(), product.getName(), product.getCategory(), product.getBrand(), product.getModel(), product.getDescription(), product.getPrice(), product.getImages(), product.getDiscount(), reservations);
    }

    public ProductDTO createProduct(Product product) {
        try {
            if (product.getDiscount() == null) product.setDiscount(0);
            Product saveProduct = productRepository.save(product);
            return new ProductDTO(saveProduct.getId(), saveProduct.getName(), saveProduct.getCategory(), saveProduct.getBrand(), saveProduct.getModel(), saveProduct.getDescription(), saveProduct.getPrice(), saveProduct.getImages(), saveProduct.getDiscount(), null);
        } catch (Exception e) {
            throw new BadRequestException("The product already exists, use another name");
        }
    }


    private List<String> uploadImagesToS3(MultipartFile[] images, String productId) {
        List<String> imageUrls = new ArrayList<>();
        String bucketName = "1023c07-grupo5";
        String folderName = "images/products/" + productId + "/";
        if (images != null) {
            for (MultipartFile image : images) {
                String imageName = folderName + generateUniqueFileName(image);

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(image.getSize());
                metadata.setContentType(image.getContentType());
                try {
                    PutObjectRequest request = new PutObjectRequest(bucketName, imageName, image.getInputStream(), metadata);
                    amazonS3.putObject(request);

                    String imageUrl = amazonS3.getUrl(bucketName, imageName).toString();
                    imageUrls.add(imageUrl);
                } catch (IOException e) {
                    throw new BadRequestException("Error uploading image");

                }
            }
        }
        return imageUrls;
    }

    private String generateUniqueFileName(MultipartFile image) {
        return UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
    }

    public ProductDTO createProductWithImages(Product product, MultipartFile[] images) {
        try {
            product = productRepository.save(product);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new BadRequestException("The product already exists, use another name");
            } else {
                e.printStackTrace();
                throw new BadRequestException("Error creating product, please contact support.");
            }
        }

        if(images != null && images.length > 0) {
            try {
                List<String> imageUrls = uploadImagesToS3(images, product.getId().toString());
                product.setImages(imageUrls);
                product = productRepository.save(product);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestException("Error uploading images, please contact support.");
            }
        }
        return productToProductDTO(product);
    }

    public ProductDTO updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        String instrument = updateProductDTO.getName();
        String category = updateProductDTO.getCategory();
        String brand = updateProductDTO.getBrand();
        String model = updateProductDTO.getModel();
        String description = updateProductDTO.getDescription();
        Float price = updateProductDTO.getPrice();
        List<String> images = updateProductDTO.getImages();
        Integer discount = updateProductDTO.getDiscount();

        if (instrument == null && category == null && brand == null && model == null && description == null && price == null && images == null && discount == null) throw new BadRequestException("No data to update");

        Product product = productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product not found"));

        if (product.getDeletedAt() != null) throw new BadRequestException("The product is deleted, please contact support.");

        if (instrument != null) product.setName(updateProductDTO.getName());
        if (category != null) product.setCategory(updateProductDTO.getCategory());
        if (brand != null) product.setBrand(updateProductDTO.getBrand());
        if (model != null) product.setModel(updateProductDTO.getModel());
        if (description != null) product.setDescription(updateProductDTO.getDescription());
        if (price != null) product.setPrice(updateProductDTO.getPrice());
        if (images != null) product.setImages(updateProductDTO.getImages());
        if (discount != null) product.setDiscount(updateProductDTO.getDiscount());

        Product saveProduct = productRepository.save(product);

        List<ProductReservationDTO> reservations = saveProduct.getReservations().stream().map(this::reservationToProductReservationDTO).toList();
        return new ProductDTO(saveProduct.getId(), saveProduct.getName(), saveProduct.getCategory(), saveProduct.getBrand(), saveProduct.getModel(), saveProduct.getDescription(), saveProduct.getPrice(), saveProduct.getImages(), saveProduct.getDiscount(), reservations);
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product not found"));
        product.setDeletedAt(new Date());
        productRepository.save(product);
    }
}
