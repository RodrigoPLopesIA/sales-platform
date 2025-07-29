package br.com.rodrigolopesdev.product_api.services;


import br.com.rodrigolopesdev.product_api.dtos.ListCategoryDTO;
import br.com.rodrigolopesdev.product_api.models.Category;
import br.com.rodrigolopesdev.product_api.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void shouldReturnAllCategories() {
        List<Category> categories = List.of(Category.builder().name("Test").build());
        var list = categories.stream().map(ListCategoryDTO::new).toList();
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        var result = categoryService.find();

        Assertions.assertThat(result).isEqualTo(list);

        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
    }
}
