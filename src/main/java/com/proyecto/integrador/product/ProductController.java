package com.proyecto.integrador.product;

import com.proyecto.integrador.product.dto.ProductDTO;
import com.proyecto.integrador.product.dto.UpdateProductDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.MyISAMStorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> find(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Float priceMin,
            @RequestParam(required = false) Float priceMax,
            @RequestParam(required = false) @Min(0) @Max(100) Integer discount,
            @RequestParam(defaultValue = "1") @Min(0) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(10) Integer limit,
            @RequestParam(defaultValue = "name") @Pattern(regexp = "^(id|name|category|brand|model|description|price|discount)$") String sort,
            @RequestParam(defaultValue = "asc") @Pattern(regexp = "^(asc|desc|random)$") String order,
            @RequestParam(required = false) @Min(1672531200) Long startDate,
            @RequestParam(required = false) @Min(1672531200) Long endDate
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findProductsUsingSQL(id, name, category, brand, model, description, priceMin, priceMax, discount, page, limit, sort, order, startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody(required = false) @Valid Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(product));
    }

//    @PostMapping("/images")
//    public ResponseEntity<?> createWithImages(
//            @RequestParam(value = "images", required = false) MultipartFile[] images,
//            @RequestParam(value = "name", required = true) @Min(3) @Max(50) @NotNull String name,
//            @RequestParam(value = "category", required = true) String category,
//            @RequestParam(value = "brand", required = false) String brand,
//            @RequestParam(value = "model", required = false) String model,
//            @RequestParam(value = "description", required = false) String description,
//            @RequestParam(value = "price", required = true) Float price,
//            @RequestParam(value = "discount", required = false) Integer discount
//    ) {
//        if (images != null) {
//            if (images.length == 0) {
//                return ResponseEntity
//                        .status(HttpStatus.BAD_REQUEST)
//                        .body("You must send at least one image");
//            }
//            if (images.length > 7) {
//                return ResponseEntity
//                        .status(HttpStatus.BAD_REQUEST)
//                        .body("Cannot upload more than 7 images");
//            }
//            for (MultipartFile image : images) {
//                if (image.getSize() > 1048576) { // 1024 * 1024 = 1MB
//                    return ResponseEntity
//                            .status(HttpStatus.BAD_REQUEST)
//                            .body("Each image must be less than 1MB in size.");
//                }
//                if (!Arrays.asList("image/jpeg", "image/png").contains(image.getContentType())) {
//                    return ResponseEntity
//                            .status(HttpStatus.BAD_REQUEST)
//                            .body("Invalid image format. Only JPEG and PNG are allowed.");
//                }
//            }
//        }
//
//        Product product = Product.builder()
//                .name(name)
//                .category(category)
//                .brand(brand)
//                .model(model)
//                .description(description)
//                .price(price)
//                .discount(discount)
//                .build();
//        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProductWithImages(product, images));
//    }


    @PostMapping("/images")
    public ResponseEntity<?> createWithImages(
            @RequestParam(value = "imagesFiles", required = false) MultipartFile[] imagesFiles,
            @Valid @ModelAttribute Product product
    ) {
        if (imagesFiles != null) {
            if (imagesFiles.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("You must send at least one image");
            }
            if (imagesFiles.length > 7) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Cannot upload more than 7 images");
            }
            for (MultipartFile image : imagesFiles) {
                if (image.getSize() > 1048576) { // 1024 * 1024 = 1MB
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Each image must be less than 1MB in size.");
                }
                if (!Arrays.asList("image/jpeg", "image/png").contains(image.getContentType())) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Invalid image format. Only JPEG and PNG are allowed.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProductWithImages(product, imagesFiles));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable(value = "id", required = false) Long id, @RequestBody @Valid UpdateProductDTO updateProductDto, Errors errors) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateProduct(id, updateProductDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.deleteProduct(id);
    }
}

