package com.proyecto.integrador.search;

import com.proyecto.integrador.product.ProductService;
import com.proyecto.integrador.product.dto.SearchProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@Validated
public class SearchController {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<SearchProductDTO> search(@RequestParam("query") String query) {
        return ResponseEntity.status(HttpStatus.OK).body(service.search(query));
    }
}
