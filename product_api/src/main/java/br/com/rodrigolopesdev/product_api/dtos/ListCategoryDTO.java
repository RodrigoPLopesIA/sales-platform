package br.com.rodrigolopesdev.product_api.dtos;

import br.com.rodrigolopesdev.product_api.models.Category;

import java.util.UUID;

public record ListCategoryDTO(UUID id, String name) {
    public ListCategoryDTO(Category category) {
        this(category.getId(), category.getName());
    }
}
