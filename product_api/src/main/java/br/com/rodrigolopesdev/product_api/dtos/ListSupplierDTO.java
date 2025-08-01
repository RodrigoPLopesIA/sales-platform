package br.com.rodrigolopesdev.product_api.dtos;

import br.com.rodrigolopesdev.product_api.models.Supplier;

import java.util.UUID;

public record ListSupplierDTO(UUID id, String name) {
    public ListSupplierDTO(Supplier savedSupplier) {
        this(savedSupplier.getId(), savedSupplier.getName());
    }
}
