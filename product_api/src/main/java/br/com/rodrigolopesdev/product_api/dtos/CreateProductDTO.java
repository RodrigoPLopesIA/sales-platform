package br.com.rodrigolopesdev.product_api.dtos;


import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductDTO(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,

        @Size(max = 500, message = "Description must be at most 500 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity cannot be negative")
        Integer quantities,

        @NotBlank(message = "SKU is required")
        @Size(max = 50, message = "SKU must be at most 50 characters")
        String sku,

        String imageUrl,

        @PositiveOrZero(message = "Weight must be positive or zero")
        Double weight,

        String dimensions,

        @NotNull(message = "Active status is required")
        Boolean isActive,

        @NotNull(message = "Supplier ID is required")
        UUID supplierId,

        @NotNull(message = "Category ID is required")
        UUID categoryId
) {}
