package br.com.rodrigolopesdev.product_api.services;

import br.com.rodrigolopesdev.product_api.dtos.ListCategoryDTO;
import br.com.rodrigolopesdev.product_api.dtos.SaveCategoryDTO;
import br.com.rodrigolopesdev.product_api.models.Category;
import br.com.rodrigolopesdev.product_api.repository.CategoryRepository;
import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private final UUID CATEGORY_ID = UUID.fromString("85260ea5-daad-4bb2-90fe-ee7e69e39d42");
    private final String CATEGORY_NAME = "Eletrônicos";

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .build();
    }

    @Test
    public void shouldReturnAllCategoriesWhenNameIsNull() {
        // Arrange
        List<Category> categories = List.of(
                Category.builder().name("Test 1").build(),
                Category.builder().name("Test 2").build()
        );

        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> pageRepository = new PageImpl<>(categories, pageable, 2);
        Category exampleCategory = Category.builder().name("").build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Category> example = Example.of(exampleCategory, matcher);
        // Mock para findAll com apenas Pageable (sem Example)
        when(categoryRepository.findAll(example, pageable)).thenReturn(pageRepository);

        // Act
        Page<ListCategoryDTO> result = categoryService.find(pageable, "");

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(categoryRepository, times(1)).findAll(example, pageable);
    }

    @Test
    public void shouldReturnAllCategoriesWhenNameIsEmpty() {
        // Arrange
        List<Category> categories = List.of(
                Category.builder().name("Test 1").build()
        );

        Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());
        Page<Category> pageRepository = new PageImpl<>(categories, pageable, 1);

        Category exampleCategory = Category.builder().name("").build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Category> example = Example.of(exampleCategory, matcher);

        when(categoryRepository.findAll(example, pageable)).thenReturn(pageRepository);

        // Act
        Page<ListCategoryDTO> result = categoryService.find(pageable, "");

        // Assert
        assertThat(result.getContent()).hasSize(1);
        verify(categoryRepository, times(1)).findAll(example, pageable);
    }

    @Test
    public void shouldReturnFilteredCategoriesByName() {
        // Arrange
        String searchName = "test";
        Pageable pageable = PageRequest.of(0, 10);

        Category exampleCategory = Category.builder().name(searchName).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<Category> example = Example.of(exampleCategory, matcher);

        List<Category> filteredCategories = List.of(
                Category.builder().name("Test 1").build(),
                Category.builder().name("Test 2").build()
        );

        Page<Category> mockedPage = new PageImpl<>(filteredCategories, pageable, 2);

        when(categoryRepository.findAll(example, pageable)).thenReturn(mockedPage);

        // Act
        Page<ListCategoryDTO> result = categoryService.find(pageable, searchName);

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).name()).isEqualTo("Test 1");
        assertThat(result.getContent().get(1).name()).isEqualTo("Test 2");
        verify(categoryRepository, times(1)).findAll(example, pageable);
    }

    @Test
    public void shouldReturnEmptyWhenNoMatchesFound() {
        // Arrange
        String searchName = "nonexistent";
        Pageable pageable = PageRequest.of(0, 10);

        Category exampleCategory = Category.builder().name(searchName).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<Category> example = Example.of(exampleCategory, matcher);

        Page<Category> emptyPage = Page.empty(pageable);

        when(categoryRepository.findAll(example, pageable)).thenReturn(emptyPage);

        // Act
        Page<ListCategoryDTO> result = categoryService.find(pageable, searchName);

        // Assert
        assertThat(result.getContent()).isEmpty();
        verify(categoryRepository, times(1)).findAll(example, pageable);
    }


        @Test
        void findById_shouldReturnCategoryWhenExists() {
            // Arrange
            when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));

            // Act
            ListCategoryDTO result = categoryService.findById(CATEGORY_ID);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(CATEGORY_ID);
            assertThat(result.name()).isEqualTo(CATEGORY_NAME);
        }

        @Test
        void findById_shouldThrowExceptionWhenCategoryNotFound() {
            // Arrange
            when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> categoryService.findById(CATEGORY_ID))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Category not found with id: " + CATEGORY_ID);
        }

        @Test
        void create_shouldSaveNewCategory() {
            // Arrange
            SaveCategoryDTO dto = new SaveCategoryDTO(CATEGORY_NAME);
            when(categoryRepository.existsByName(CATEGORY_NAME)).thenReturn(false);
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            // Act
            ListCategoryDTO result = categoryService.create(dto);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(CATEGORY_ID);
            assertThat(result.name()).isEqualTo(CATEGORY_NAME);
            verify(categoryRepository).existsByName(CATEGORY_NAME);
            verify(categoryRepository).save(any(Category.class));
        }

        @Test
        void create_shouldThrowExceptionWhenNameAlreadyExists() {
            // Arrange
            SaveCategoryDTO dto = new SaveCategoryDTO(CATEGORY_NAME);
            when(categoryRepository.existsByName(CATEGORY_NAME)).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.create(dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Category already exists with name: " + CATEGORY_NAME);
        }

        @Test
        void update_shouldUpdateExistingCategory() {
            // Arrange
            String newName = "Eletrodomésticos";
            SaveCategoryDTO dto = new SaveCategoryDTO(newName);

            when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
            when(categoryRepository.existsByName(newName)).thenReturn(false);
            when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            ListCategoryDTO result = categoryService.update(CATEGORY_ID, dto);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(CATEGORY_ID);
            assertThat(result.name()).isEqualTo(newName);
            verify(categoryRepository).findById(CATEGORY_ID);
            verify(categoryRepository).existsByName(newName);
            verify(categoryRepository).save(any(Category.class));
        }

        @Test
        void update_shouldThrowExceptionWhenCategoryNotFound() {
            // Arrange
            SaveCategoryDTO dto = new SaveCategoryDTO("Novo Nome");
            when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> categoryService.update(CATEGORY_ID, dto))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Category not found with id: " + CATEGORY_ID);
        }

        @Test
        void update_shouldThrowExceptionWhenNewNameAlreadyExists() {
            // Arrange
            String newName = "Eletrodomésticos";
            SaveCategoryDTO dto = new SaveCategoryDTO(newName);

            when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
            when(categoryRepository.existsByName(newName)).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.update(CATEGORY_ID, dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Category already exists with name: " + newName);
        }

        @Test
        void delete_shouldDeleteCategoryWhenExists() {
            // Arrange
            when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(true);

            // Act
            categoryService.delete(CATEGORY_ID);

            // Assert
            verify(categoryRepository).existsById(CATEGORY_ID);
            verify(categoryRepository).deleteById(CATEGORY_ID);
        }

        @Test
        void delete_shouldThrowExceptionWhenCategoryNotFound() {
            // Arrange
            when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> categoryService.delete(CATEGORY_ID))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Category not found with id: " + CATEGORY_ID);
        }

        @Test
        void existsById_shouldReturnTrueWhenCategoryExists() {
            // Arrange
            when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(true);

            // Act
            boolean result = categoryService.existsById(CATEGORY_ID);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void existsById_shouldReturnFalseWhenCategoryNotExists() {
            // Arrange
            when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(false);

            // Act
            boolean result = categoryService.existsById(CATEGORY_ID);

            // Assert
            assertThat(result).isFalse();
        }

}
