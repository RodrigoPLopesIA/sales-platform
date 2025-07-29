package br.com.rodrigolopesdev.product_api.services;

import br.com.rodrigolopesdev.product_api.dtos.ListCategoryDTO;
import br.com.rodrigolopesdev.product_api.models.Category;
import br.com.rodrigolopesdev.product_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<ListCategoryDTO> find(Pageable page) {


        return this.categoryRepository.findAll(page).map(ListCategoryDTO::new);
    }
}
