package com.proyecto.integrador.favorites;

import com.proyecto.integrador.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // Endpoint para agregar un instrumento a favoritos
    @PostMapping("/add")
    public ResponseEntity<String> addFavorite(@RequestParam String userEmail, @RequestParam Long productId) {
        favoriteService.addFavorite(userEmail, productId);
        return ResponseEntity.status(HttpStatus.OK).body("Instrumento agregado a favoritos");
    }

    // Endpoint para remover un instrumento de favoritos
    @Transactional
    @PostMapping("/remove")
    public ResponseEntity<String> removeFavorite(@RequestParam String userEmail, @RequestParam Long productId) {
        favoriteService.removeFavorite(userEmail, productId);
        return ResponseEntity.status(HttpStatus.OK).body("Instrumento removido de favoritos");
    }

    // Endpoint para listar todos los instrumentos favoritos de un usuario
    @GetMapping("/list")
    public ResponseEntity<List<Product>> listFavorites(@RequestParam String userEmail) {
        List<Product> favorites = favoriteService.listFavorites(userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(favorites);
    }
}
