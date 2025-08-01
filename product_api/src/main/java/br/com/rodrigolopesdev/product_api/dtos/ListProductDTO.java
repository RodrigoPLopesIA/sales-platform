package br.com.rodrigolopesdev.product_api.dtos;

import br.com.rodrigolopesdev.product_api.models.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ListProductDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer quantities,
        String sku,
        String imageUrl,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        UUID supplierId,
        String supplierName,
        UUID categoryId,
        String categoryName
) {
    public ListProductDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantities(),
                product.getSku(),
                product.getImageUrl(),
                product.getIsActive(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getSupplier() != null ? product.getSupplier().getId() : null,
                product.getSupplier() != null ? product.getSupplier().getName() : null,
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null
        );
    }
}
