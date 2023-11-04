package com.proyecto.integrador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.product.Product;
import com.proyecto.integrador.product.ProductService;
import com.proyecto.integrador.product.dto.ProductDTO;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //addFilters = false --> No nos tenemos que autenticar
public class ProductTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductService productService;

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
    public void crearProductoRepetidoDeberiaFallar() throws Exception {
        // Tenemos un producto existente con el nombre "Guitar"
        Product productoExistente = new Product();
        productoExistente.setName("Guitar");
        productService.createProduct(productoExistente);

        // Intenta agregar un producto con el mismo nombre
        Product nuevoProducto = new Product();
        nuevoProducto.setName("Guitar");

        // Realiza la solicitud POST para agregar el nuevo producto
        MvcResult resultado = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(nuevoProducto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Espera un fallo
                .andReturn();
        String responseBody = resultado.getResponse().getContentAsString();
        assertTrue(responseBody.contains("The product already exists, use another name"));
    }

    // Convierte un objeto a una cadena JSON
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
