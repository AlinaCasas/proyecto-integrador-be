package com.proyecto.integrador.favorites;

import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.product.ProductController;
import com.proyecto.integrador.product.ProductRepository;
import com.proyecto.integrador.user.User;
import com.proyecto.integrador.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class FavoriteService {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);
    @Autowired
    private UserFavoriteProductRepository userFavoriteProductRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addFavorite(String userEmail, Long productId) {
        logger.debug("Correo electrónico proporcionado: {}", userEmail);
        User user = userRepository.findByEmail(userEmail.trim())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con el correo: " + userEmail));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Instrumento no encontrado"));

        UserFavoriteProduct userFavoriteProduct = new UserFavoriteProduct();
        userFavoriteProduct.setUser(user);
        userFavoriteProduct.setProduct(product);

        userFavoriteProductRepository.save(userFavoriteProduct);
    }

    @Transactional
    public void removeFavorite(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Instrumento no encontrado"));

        userFavoriteProductRepository.deleteByUserAndProduct(user, product);
    }

    public List<Product> listFavorites(String userEmail) {
        List<UserFavoriteProduct> userFavoriteProducts = userFavoriteProductRepository.findByUserEmail(userEmail);
        return userFavoriteProducts.stream().map(UserFavoriteProduct::getProduct).collect(Collectors.toList());
    }
}
