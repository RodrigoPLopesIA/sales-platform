package br.com.rodrigolopesdev.product_api.repository;

import br.com.rodrigolopesdev.product_api.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
