package com.proyecto.integrador.product;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.util.List;

public class ProductSpecification {

    public static Specification<Product> hasId(Long id) {
        return (root, query, criteriaBuilder) ->
                id == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("id"), id);
    }
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                category == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.like(root.get("category"), "%" + category + "%");
    }

    public static Specification<Product> hasBrand(String brand) {
    return (root, query, criteriaBuilder) ->
            brand == null ? criteriaBuilder.conjunction() :
                    criteriaBuilder.like(root.get("brand"), "%" + brand + "%");
    }

    public static Specification<Product> hasModel(String model) {
    return (root, query, criteriaBuilder) ->
            model == null ? criteriaBuilder.conjunction() :
                    criteriaBuilder.like(root.get("model"), "%" + model + "%");
    }

    public static Specification<Product> hasDescription(String description) {
    return (root, query, criteriaBuilder) ->
            description == null ? criteriaBuilder.conjunction() :
                    criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Product> priceGreaterThanOrEqualTo(Float priceMin) {
        return (root, query, criteriaBuilder) ->
                priceMin == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin);
    }

    public static Specification<Product> priceLessThanOrEqualTo(Float priceMax) {
        return (root, query, criteriaBuilder) ->
                priceMax == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax);
    }

    public static Specification<Product> discountGreaterThanOrEqualTo(Integer discount) {
        return (root, query, criteriaBuilder) ->
                discount == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("discount"), discount);
    }

    public static Specification<Product> hasIdIn(List<Long> idList) {
        return (root, query, criteriaBuilder) -> {
            if (idList == null || idList.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                return root.get("id").in(idList);
            }
        };
    }

}
