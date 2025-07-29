package br.com.rodrigolopesdev.product_api.services;

import br.com.rodrigolopesdev.product_api.dtos.ListCategoryDTO;
import br.com.rodrigolopesdev.product_api.dtos.SaveCategoryDTO;
import br.com.rodrigolopesdev.product_api.models.Category;
import br.com.rodrigolopesdev.product_api.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<ListCategoryDTO> find(Pageable pageable, String name) {

        Category exampleCategory = Category.builder().name(name).build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<Category> example = Example.of(exampleCategory, matcher);

        return categoryRepository.findAll(example, pageable).map(ListCategoryDTO::new);
    }

    public ListCategoryDTO findById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        return new ListCategoryDTO(category);
    }


    public ListCategoryDTO create(SaveCategoryDTO dto) {
        // Verifica se já existe categoria com o mesmo nome
        if (categoryRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Category already exists with name: " + dto.name());
        }

        Category category = Category.builder()
                .name(dto.name())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return new ListCategoryDTO(savedCategory);
    }

    public ListCategoryDTO update(UUID id, SaveCategoryDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        // Verifica se o novo nome já existe (excluindo a própria categoria)
        if (!category.getName().equals(dto.name()) &&
                categoryRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Category already exists with name: " + dto.name());
        }

        category.setName(dto.name());
        Category updatedCategory = categoryRepository.save(category);
        return new ListCategoryDTO(updatedCategory);
    }

    public void delete(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return categoryRepository.existsById(id);
    }
}
