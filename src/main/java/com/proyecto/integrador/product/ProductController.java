package com.proyecto.integrador.product;

import com.proyecto.integrador.category.Category;
import com.proyecto.integrador.product.dto.ProductDTO;
import com.proyecto.integrador.product.dto.UpdateProductDTO;
import jakarta.persistence.EntityNotFoundException;
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
import com.proyecto.integrador.category.CategoryRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService service;
    @Autowired
    private CategoryRepository categoryRepository;

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
    public ResponseEntity<?> create(
            @RequestParam(value = "categoryId", required = true) Long categoryId,
            @RequestParam(value = "imagesFiles", required = false) MultipartFile[] imagesFiles,
            @Valid @ModelAttribute Product product
    ) {
        try {
            if (imagesFiles != null) {
                HashMap<String, String> imagesError = validateImages(imagesFiles);
                if (imagesError != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(imagesError);
                }

                // Buscar la categoría por ID
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

                // Asignar la categoría al producto
                product.setCategory(category);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(product, imagesFiles));
        } catch (Exception e) {
            e.printStackTrace(); // Imprime la excepción en la consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable(value = "id", required = false) Long id,
            @RequestParam(value = "imagesFiles", required = false) MultipartFile[] imagesFiles,
            @Valid @ModelAttribute UpdateProductDTO updateProductDto
    ) {
        if (imagesFiles != null) {
            HashMap<String, String> imagesError = validateImages(imagesFiles);
            if (imagesError != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(imagesError);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.updateProduct(id, updateProductDto, imagesFiles));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.deleteProduct(id);
    }

    private HashMap<String, String> validateImages(MultipartFile[] imagesFiles) {

        HashMap<String, String> errors = new HashMap<>();
        if (imagesFiles.length == 0) {
            errors.put("error", "At least one image is required");
            return errors;
        }
        if (imagesFiles.length > 7) {
            errors.put("error", "Maximum 7 images are allowed");
            return errors;
        }
        for (MultipartFile image : imagesFiles) {
            if (image.getSize() > 1048576) { // 1024 * 1024 = 1MB
                errors.put("error", "Maximum image size is 1MB");
                return errors;
            }
            if (!Arrays.asList("image/jpeg", "image/png", "image/webp").contains(image.getContentType())) {
                errors.put("error", "Only JPG, PNG and WEBP images are allowed");
                return errors;
            }
        }
        return null;
    }
}
