package com.proyecto.integrador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.exceptions.BadRequestException;
import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.product.ProductService;
import com.proyecto.integrador.product.dto.ProductDTO;
import com.proyecto.integrador.token.Token;
import com.proyecto.integrador.token.TokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.junit.Assert.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //addFilters = false --> No nos tenemos que autenticar
public class ProductTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductService productService;
    @Autowired
    private TokenRepository tokenRepository;

    // Cargamos los datos iniciales
    public void cargadorDeDatos(){
        ProductDTO productoAgregado = productService.createProduct(
                new Product( 1L, "Guitar", "Musical Instruments", "Fender", "Stratocaster", "A classic electric guitar.", 799.99f, Arrays.asList("[ ]"),10,null));
    }

    @Test
    public void listarProductosTest() throws Exception{
        cargadorDeDatos();
        MvcResult resultado= mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();
        assertFalse(resultado.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void crearProductoComoAdministrador() throws Exception {
        cargadorDeDatos();

        // Obtenemos el token de administrador
        Token adminToken = tokenRepository.findByToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLmNvbSIsImlhdCI6MTY5OTM1NDUxMSwiZXhwIjoxNjk5NDQwOTExfQ.hgKL2iLAK6hBUDrqzTaa-UVMtKEAeVxEThAowAJMI0Y").orElse(null);

        // Nos aseguramos que tenemos un token valido
        assertNotNull(adminToken);

        // Creo un producto
        Product nuevoProducto = new Product();
        nuevoProducto.setName("El mejor piano");
        nuevoProducto.setCategory("Piano");
        nuevoProducto.setPrice(199.99f);

        // Realizamos la solicitud POST con el token de administrador
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .header("Authorization", "Bearer " + adminToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(nuevoProducto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated()); // Esperamos un estado 201
    }

    @Test
    public void crearProductoRepetidoDeberiaFallar() throws Exception {

        // Obtenemos el token de administrador
        Token adminToken = tokenRepository.findByToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLmNvbSIsImlhdCI6MTY5OTM1NDUxMSwiZXhwIjoxNjk5NDQwOTExfQ.hgKL2iLAK6hBUDrqzTaa-UVMtKEAeVxEThAowAJMI0Y").orElse(null);

        // Creo un producto con el nombre "Guitar" en la base de datos
        Product productoExistente = new Product();
        productoExistente.setName("Guitar");
        productService.createProduct(productoExistente);

        // Creo otro producto con el mismo nombre
        Product nuevoProducto = new Product();
        nuevoProducto.setName("Guitar");

        // Realizo la solicitud POST para agregar el nuevo producto
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(nuevoProducto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Esperamos un fallo
                .andReturn();
    }

    @Test
    public void deleteProductTest() throws Exception {
        // Agregamos un producto a la base de datos
        ProductDTO productoAgregado = productService.createProduct(
                new Product(1L, "Guitar", "Musical Instruments", "Fender", "Stratocaster", "A classic electric guitar.", 799.99f, Arrays.asList("[ ]"), 10, null));

        // Obtenemos el ID del producto que se acaba de agregar
        Long productId = productoAgregado.getId();

        // Llamamos al método para eliminar el producto
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/" + productId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Intento encontrar el producto por ID (debe arrojar una excepción)
        assertThrows(BadRequestException.class, () -> productService.findProductById(productId));
    }


    // Método para convertir un objeto a una cadena JSON
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
