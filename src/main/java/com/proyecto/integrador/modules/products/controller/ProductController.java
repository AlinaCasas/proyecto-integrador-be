package com.proyecto.integrador.modules.products.controller;

import com.proyecto.integrador.modules.products.dto.UpdateProductDTO;
import com.proyecto.integrador.modules.products.dto.ProductDTO;
import com.proyecto.integrador.modules.products.entity.Product;
import com.proyecto.integrador.modules.products.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> find() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProducts());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody(required = false) @Valid Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable(value = "id", required = false) String id, @RequestBody @Valid UpdateProductDTO updateProductDto, Errors errors) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id, updateProductDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        productService.deleteProduct(id);
    }
}
