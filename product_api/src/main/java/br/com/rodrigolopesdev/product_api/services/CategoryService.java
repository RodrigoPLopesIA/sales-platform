package br.com.rodrigolopesdev.product_api.services;

import br.com.rodrigolopesdev.product_api.dtos.ListCategoryDTO;
import br.com.rodrigolopesdev.product_api.models.Category;
import br.com.rodrigolopesdev.product_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ListCategoryDTO> find() {
        return this.categoryRepository.findAll().stream().map(ListCategoryDTO::new).toList();
    }
}
