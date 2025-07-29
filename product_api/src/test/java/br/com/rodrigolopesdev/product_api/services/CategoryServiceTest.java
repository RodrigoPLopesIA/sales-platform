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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void shouldReturnAllCategories() {
        // Arrange
        List<Category> categories = List.of(Category.builder().name("Test").build());
        List<ListCategoryDTO> categoriesDto = categories.stream()
                .map(ListCategoryDTO::new)
                .toList();

        Pageable pageable = PageRequest.of(1, 20);
        Page<Category> pageRepository = new PageImpl<>(categories, pageable, 1);
        Page<ListCategoryDTO> expectedPage = new PageImpl<>(categoriesDto, pageable, 1);

        // Mock
        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(pageRepository);

        // Act
        Page<ListCategoryDTO> result = categoryService.find(pageable);

        // Assert
        Assertions.assertThat(result.getContent()).isEqualTo(expectedPage.getContent());
        Assertions.assertThat(result.getTotalElements()).isEqualTo(expectedPage.getTotalElements());
        Assertions.assertThat(result.getPageable()).isEqualTo(expectedPage.getPageable());

        Mockito.verify(categoryRepository, Mockito.times(1)).findAll(pageable);
    }

}
