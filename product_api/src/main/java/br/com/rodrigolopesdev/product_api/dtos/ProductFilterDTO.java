package br.com.rodrigolopesdev.product_api.dtos;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductFilterDTO(
        String name,
        String sku,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean isActive,
        Boolean inStockOnly,
        String supplierName,
        String categoryName
) {
    public static ProductFilterDTO of(
            String name,
            String sku,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isActive,
            Boolean inStockOnly,
            String supplierName,
            String categoryName
    ) {
        return new ProductFilterDTO(
                name,
                sku,
                minPrice,
                maxPrice,
                isActive,
                inStockOnly,
                supplierName,
                categoryName
        );
    }

    public static ProductFilterDTO empty() {
        return new ProductFilterDTO(
                null, null, null, null, null, null,
                null, null
        );
    }
}