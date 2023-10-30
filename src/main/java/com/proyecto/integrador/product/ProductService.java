package com.proyecto.integrador.product;

import com.proyecto.integrador.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> findProducts(){
        return productRepository.findAllProductsBy();
    }

    public ProductDTO createProduct(Product product) {
        try {
            if (product.getDiscount() == null) product.setDiscount(0);
            Product saveProduct = productRepository.save(product);
            return new ProductDTO(saveProduct.getId(), saveProduct.getName(), saveProduct.getCategory(), saveProduct.getBrand(), saveProduct.getModel(), saveProduct.getDescription(), saveProduct.getPrice(), saveProduct.getImages(), saveProduct.getDiscount());
        } catch (Exception e) {
            throw new BadRequestException("The product already exists, use another name");
        }
    }

    public ProductDTO updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        String instrument = updateProductDTO.getName();
        String category = updateProductDTO.getCategory();
        String brand = updateProductDTO.getBrand();
        String model = updateProductDTO.getModel();
        String description = updateProductDTO.getDescription();
        Float price = updateProductDTO.getPrice();
        List<String> images = updateProductDTO.getImages();
        Integer discount = updateProductDTO.getDiscount();

        if (instrument == null && category == null && brand == null && model == null && description == null && price == null && images == null && discount == null) throw new BadRequestException("No data to update");

        Product product = productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product not found"));

        if (product.getDeletedAt() != null) throw new BadRequestException("The product is deleted, please contact support.");

        if (instrument != null) product.setName(updateProductDTO.getName());
        if (category != null) product.setCategory(updateProductDTO.getCategory());
        if (brand != null) product.setBrand(updateProductDTO.getBrand());
        if (model != null) product.setModel(updateProductDTO.getModel());
        if (description != null) product.setDescription(updateProductDTO.getDescription());
        if (price != null) product.setPrice(updateProductDTO.getPrice());
        if (images != null) product.setImages(updateProductDTO.getImages());
        if (discount != null) product.setDiscount(updateProductDTO.getDiscount());

        Product saveProduct = productRepository.save(product);
        return new ProductDTO(saveProduct.getId(), saveProduct.getName(), saveProduct.getCategory(), saveProduct.getBrand(), saveProduct.getModel(), saveProduct.getDescription(), saveProduct.getPrice(), saveProduct.getImages(), saveProduct.getDiscount());
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product not found"));
        productRepository.deleteById(id);
    }
}
