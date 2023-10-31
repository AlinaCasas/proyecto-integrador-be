package com.proyecto.integrador.product;

import com.proyecto.integrador.product.dto.ProductDTO;
import com.proyecto.integrador.product.dto.UpdateProductDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> find() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findProducts());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody(required = false) @Valid Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(product));
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

