package com.proyecto.integrador.product;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.proyecto.integrador.category.Category;
import com.proyecto.integrador.category.CategoryRepository;
import com.proyecto.integrador.exceptions.BadRequestException;
import com.proyecto.integrador.product.dto.ProductDTO;
import com.proyecto.integrador.product.dto.ProductReservationDTO;
import com.proyecto.integrador.product.dto.UpdateProductDTO;
import com.proyecto.integrador.reservation.Reservation;
import com.proyecto.integrador.reservation.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AmazonS3 amazonS3;

    public Page<ProductDTO> findProductsUsingSQL(Long id, String name, String category, String brand, String model, String description, Float priceMin, Float priceMax, Integer discount, int page, int limit, String sort, String order, Long startDate, Long endDate) {
        if (startDate != null && endDate == null || startDate == null && endDate != null) {
            throw new BadRequestException("If you want to filter by date, you must send both start and end date");
        }
        if (startDate != null && startDate > endDate) {
            throw new BadRequestException("The start date must be less than the end date");
        }
        page = page == 0 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(page, limit);

        // TODO add rating filter and order by rating

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
        return new ProductDTO(product.getId(), product.getName(), product.getCategory().getName(), product.getBrand(), product.getModel(), product.getDescription(), product.getPrice(), product.getRating(), product.getRatingCount(), product.getImages(), product.getDiscount(), reservations);
    }



    public ProductDTO createProduct(Product product, MultipartFile[] imagesFiles) {
        if (product.getImages() != null && product.getImages().size() + imagesFiles.length > 7) {
            throw new BadRequestException("Product can't have more than 7 images");
        }

        product.setDiscount(Optional.ofNullable(product.getDiscount()).orElse(0));
        Product savedProduct = trySaveProduct(product);

        if (imagesFiles != null && imagesFiles.length > 0) {
            List<String> imageUrls = uploadImages(imagesFiles, savedProduct.getId().toString());
            List<String> currentImages = savedProduct.getImages();
            currentImages.addAll(imageUrls);
            savedProduct.setImages(currentImages);
            savedProduct = productRepository.save(savedProduct);
        }
        return productToProductDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, UpdateProductDTO updateProductDTO, MultipartFile[] imagesFiles) {

        String name = updateProductDTO.getName();
        String category = updateProductDTO.getCategory();
        String brand = updateProductDTO.getBrand();
        String model = updateProductDTO.getModel();
        String description = updateProductDTO.getDescription();
        Float price = updateProductDTO.getPrice();
        List<String> images = updateProductDTO.getImages();
        Integer discount = updateProductDTO.getDiscount();

        if (name == null && category == null && brand == null && model == null && description == null && price == null && images == null && discount == null && imagesFiles == null) throw new BadRequestException("No data to update");

        Product product = productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product not found"));

        Product productByName = productRepository.findByName(name);

        if (productByName != null && !productByName.getId().equals(id)) throw new BadRequestException("The product already exists, use another name");

        if (product.getDeletedAt() != null) throw new BadRequestException("The product is deleted, please contact support.");

        if (name != null) product.setName(updateProductDTO.getName());
        if (updateProductDTO.getCategory() != null) {
            String categoryName = updateProductDTO.getCategory();
            Category foundCategory = categoryRepository.findByName(categoryName).orElseThrow(() -> new BadRequestException("Category not found with name: " + categoryName));

            product.setCategory(foundCategory);
        }
        if (brand != null) product.setBrand(updateProductDTO.getBrand());
        if (model != null) product.setModel(updateProductDTO.getModel());
        if (description != null) product.setDescription(updateProductDTO.getDescription());
        if (price != null) product.setPrice(updateProductDTO.getPrice());
        if (discount != null) product.setDiscount(updateProductDTO.getDiscount());

        List<String> imagesToDelete = new ArrayList<>();
        if (images != null) {
            List<String> currentImages = product.getImages();
            if (currentImages != null && !currentImages.isEmpty()) {
                for (String currentImage : currentImages) {
                    if (!images.contains(currentImage)) {
                        imagesToDelete.add(currentImage);
                    }
                }
            }
            product.setImages(images);
        }

        if (imagesFiles != null && imagesFiles.length > 0) {
            List<String> currentImages = product.getImages();
            if (currentImages != null && currentImages.size() + imagesFiles.length > 7) {
                throw new BadRequestException("Product can't have more than 7 images");
            }

            List<String> imageUrls = uploadImages(imagesFiles, product.getId().toString());
            currentImages.addAll(imageUrls);
            product.setImages(currentImages);
        }

        Product saveProduct = productRepository.save(product);

        if (!imagesToDelete.isEmpty()) {
            deleteImages(imagesToDelete);
        }

        List<ProductReservationDTO> reservations = saveProduct.getReservations().stream().map(this::reservationToProductReservationDTO).toList();
        return new ProductDTO(saveProduct.getId(), saveProduct.getName(), saveProduct.getCategory().getName(), saveProduct.getBrand(), saveProduct.getModel(), saveProduct.getDescription(), saveProduct.getPrice(), saveProduct.getRating(), saveProduct.getRatingCount(), saveProduct.getImages(), saveProduct.getDiscount(), reservations);
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product not found"));
        product.setDeletedAt(new Date());
        productRepository.save(product);
    }

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
                .category(product.getCategory() != null ? product.getCategory().getName() : null)
                .brand(product.getBrand())
                .model(product.getModel())
                .description(product.getDescription())
                .price(product.getPrice())
                .rating(product.getRating())
                .ratingCount(product.getRatingCount())
                .images(product.getImages())
                .discount(product.getDiscount())
                .reservations(reservations)
                .build();
    }

    private Product trySaveProduct(Product product) {
        try {
            if (product.getImages() == null) product.setImages(new ArrayList<>());
            return productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new BadRequestException("The product already exists, use another name");
            } else {
                e.printStackTrace();
                throw new BadRequestException("Error creating product, please contact support.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Unexpected error, please contact support.");
        }
    }

    private List<String> uploadImages(MultipartFile[] images, String productId) {
            if (images != null) {
                List<String> imageUrls = new ArrayList<>();
                String bucketName = "1023c07-grupo5";
                String folderName = "images/products/" + productId + "/";
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
                return imageUrls;
            } else {
                return null;
            }
        }

    private boolean deleteImages(List<String> images) {
        if (images != null && !images.isEmpty()) {
            String bucketName = "1023c07-grupo5";
            List<String> imagesToDelete = new ArrayList<>();
            for (String image : images) {
                String[] parts = image.split("amazonaws.com/");
                String imageName = parts[parts.length - 1];
                imagesToDelete.add(imageName);
            }
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(imagesToDelete.toArray(new String[0]));
            amazonS3.deleteObjects(deleteObjectsRequest);
            return true;
        } else {
            return false;
        }
    }

    private String generateUniqueFileName(MultipartFile image) {
        return UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
    }
}
