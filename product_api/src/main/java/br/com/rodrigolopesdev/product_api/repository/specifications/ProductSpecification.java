package br.com.rodrigolopesdev.product_api.repository.specifications;

import br.com.rodrigolopesdev.product_api.models.Product;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> conjunction() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<Product> withName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
    public static Specification<Product> withSKU(String sku) {
        return (root, query, cb) -> {
            if (sku == null || sku.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + sku.toLowerCase() + "%");
        };
    }

    public static Specification<Product> withMinPrice(BigDecimal minPrice) {
        return (root, query, cb) -> {
            if (minPrice == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
        };
    }

    public static Specification<Product> withMaxPrice(BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (maxPrice == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
    public static Specification<Product> isActive(boolean isActive) {
        return (root, query, cb) -> cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<Product> inStock() {
        return (root, query, cb) -> cb.greaterThan(root.get("quantities"), 0);
    }


    public static Specification<Product> withSupplierName(String supplierName) {
        return (root, query, cb) -> {
            if (supplierName == null || supplierName.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("supplier").get("name")),
                    "%" + supplierName.toLowerCase() + "%"
            );
        };
    }
    public static Specification<Product> withCategoryName(String categoryName) {
        return (root, query, cb) -> {
            if (categoryName == null || categoryName.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("category").get("name")),
                    "%" + categoryName.toLowerCase() + "%"
            );
        };
    }
}