package com.proyecto.integrador.product;
import com.proyecto.integrador.exceptions.BadRequestException;
import com.proyecto.integrador.product.dto.ProductDTO;
import com.proyecto.integrador.reservation.Reservation;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceTest {

    @Autowired
    ProductService productService;
    @MockBean
    ProductRepository productRepository;

    @Test
    void itShouldCreateAProduct() {
        Product productMock = new Product();
        productMock.setName("Product Name");

        Mockito.when(productRepository.save(productMock)).thenReturn(productMock);

        ProductDTO registeredProduct = productService.createProduct(productMock);

        assertNotNull(registeredProduct);
    }
}