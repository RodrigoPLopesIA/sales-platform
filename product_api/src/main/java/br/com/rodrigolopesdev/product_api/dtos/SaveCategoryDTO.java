package br.com.rodrigolopesdev.product_api.dtos;

import jakarta.validation.constraints.NotBlank;

public record SaveCategoryDTO(@NotBlank String name) {
}