package br.com.rodrigolopesdev.product_api.services;

import br.com.rodrigolopesdev.product_api.dtos.ListSupplierDTO;
import br.com.rodrigolopesdev.product_api.dtos.SaveSupplierDTO;
import br.com.rodrigolopesdev.product_api.models.Supplier;
import br.com.rodrigolopesdev.product_api.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;
    private final UUID CATEGORY_ID = UUID.fromString("85260ea5-daad-4bb2-90fe-ee7e69e39d42");
    private final String CATEGORY_NAME = "Eletrônicos";

    @BeforeEach
    void setUp() {
        supplier = Supplier.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .build();
    }

    @Test
    public void shouldReturnAllCategoriesWhenNameIsNull() {
        // Arrange
        List<Supplier> suppliers = List.of(
                Supplier.builder().name("Test 1").build(),
                Supplier.builder().name("Test 2").build()
        );

        Pageable pageable = PageRequest.of(0, 10);
        Page<Supplier> pageRepository = new PageImpl<>(suppliers, pageable, 2);
        Supplier exampleSupplier = Supplier.builder().name("").build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Supplier> example = Example.of(exampleSupplier, matcher);

        when(supplierRepository.findAll(example, pageable)).thenReturn(pageRepository);

        // Act
        Page<ListSupplierDTO> result = supplierService.find(pageable, "");

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(supplierRepository, times(1)).findAll(example, pageable);
    }

    @Test
    public void shouldReturnAllCategoriesWhenNameIsEmpty() {
        // Arrange
        List<Supplier> suppliers = List.of(
                Supplier.builder().name("Test 1").build()
        );

        Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());
        Page<Supplier> pageRepository = new PageImpl<>(suppliers, pageable, 1);

        Supplier exampleSupplier = Supplier.builder().name("").build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Supplier> example = Example.of(exampleSupplier, matcher);

        when(supplierRepository.findAll(example, pageable)).thenReturn(pageRepository);

        // Act
        Page<ListSupplierDTO> result = supplierService.find(pageable, "");

        // Assert
        assertThat(result.getContent()).hasSize(1);
        verify(supplierRepository, times(1)).findAll(example, pageable);
    }

    @Test
    public void shouldReturnFilteredCategoriesByName() {
        // Arrange
        String searchName = "test";
        Pageable pageable = PageRequest.of(0, 10);

        Supplier exampleSupplier = Supplier.builder().name(searchName).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<Supplier> example = Example.of(exampleSupplier, matcher);

        List<Supplier> filteredCategories = List.of(
                Supplier.builder().name("Test 1").build(),
                Supplier.builder().name("Test 2").build()
        );

        Page<Supplier> mockedPage = new PageImpl<>(filteredCategories, pageable, 2);

        when(supplierRepository.findAll(example, pageable)).thenReturn(mockedPage);

        // Act
        Page<ListSupplierDTO> result = supplierService.find(pageable, searchName);

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).name()).isEqualTo("Test 1");
        assertThat(result.getContent().get(1).name()).isEqualTo("Test 2");
        verify(supplierRepository, times(1)).findAll(example, pageable);
    }

    @Test
    public void shouldReturnEmptyWhenNoMatchesFound() {
        // Arrange
        String searchName = "nonexistent";
        Pageable pageable = PageRequest.of(0, 10);

        Supplier exampleSupplier = Supplier.builder().name(searchName).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<Supplier> example = Example.of(exampleSupplier, matcher);

        Page<Supplier> emptyPage = Page.empty(pageable);

        when(supplierRepository.findAll(example, pageable)).thenReturn(emptyPage);

        // Act
        Page<ListSupplierDTO> result = supplierService.find(pageable, searchName);

        // Assert
        assertThat(result.getContent()).isEmpty();
        verify(supplierRepository, times(1)).findAll(example, pageable);
    }


        @Test
        void findById_shouldReturnSupplierWhenExists() {
            // Arrange
            when(supplierRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(supplier));

            // Act
            ListSupplierDTO result = supplierService.findById(CATEGORY_ID);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(CATEGORY_ID);
            assertThat(result.name()).isEqualTo(CATEGORY_NAME);
        }

        @Test
        void findById_shouldThrowExceptionWhenSupplierNotFound() {
            // Arrange
            when(supplierRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> supplierService.findById(CATEGORY_ID))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Supplier not found with id: " + CATEGORY_ID);
        }

        @Test
        void create_shouldSaveNewSupplier() {
            // Arrange
            SaveSupplierDTO dto = new SaveSupplierDTO(CATEGORY_NAME);
            when(supplierRepository.existsByName(CATEGORY_NAME)).thenReturn(false);
            when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

            // Act
            ListSupplierDTO result = supplierService.create(dto);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(CATEGORY_ID);
            assertThat(result.name()).isEqualTo(CATEGORY_NAME);
            verify(supplierRepository).existsByName(CATEGORY_NAME);
            verify(supplierRepository).save(any(Supplier.class));
        }

        @Test
        void create_shouldThrowExceptionWhenNameAlreadyExists() {
            // Arrange
            SaveSupplierDTO dto = new SaveSupplierDTO(CATEGORY_NAME);
            when(supplierRepository.existsByName(CATEGORY_NAME)).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> supplierService.create(dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Supplier already exists with name: " + CATEGORY_NAME);
        }

        @Test
        void update_shouldUpdateExistingSupplier() {
            // Arrange
            String newName = "Eletrodomésticos";
            SaveSupplierDTO dto = new SaveSupplierDTO(newName);

            when(supplierRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(supplier));
            when(supplierRepository.existsByName(newName)).thenReturn(false);
            when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            ListSupplierDTO result = supplierService.update(CATEGORY_ID, dto);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(CATEGORY_ID);
            assertThat(result.name()).isEqualTo(newName);
            verify(supplierRepository).findById(CATEGORY_ID);
            verify(supplierRepository).existsByName(newName);
            verify(supplierRepository).save(any(Supplier.class));
        }

        @Test
        void update_shouldThrowExceptionWhenSupplierNotFound() {
            // Arrange
            SaveSupplierDTO dto = new SaveSupplierDTO("Novo Nome");
            when(supplierRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> supplierService.update(CATEGORY_ID, dto))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Supplier not found with id: " + CATEGORY_ID);
        }

        @Test
        void update_shouldThrowExceptionWhenNewNameAlreadyExists() {
            // Arrange
            String newName = "Eletrodomésticos";
            SaveSupplierDTO dto = new SaveSupplierDTO(newName);

            when(supplierRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(supplier));
            when(supplierRepository.existsByName(newName)).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> supplierService.update(CATEGORY_ID, dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Supplier already exists with name: " + newName);
        }

        @Test
        void delete_shouldDeleteSupplierWhenExists() {
            // Arrange
            when(supplierRepository.existsById(CATEGORY_ID)).thenReturn(true);

            // Act
            supplierService.delete(CATEGORY_ID);

            // Assert
            verify(supplierRepository).existsById(CATEGORY_ID);
            verify(supplierRepository).deleteById(CATEGORY_ID);
        }

        @Test
        void delete_shouldThrowExceptionWhenSupplierNotFound() {
            // Arrange
            when(supplierRepository.existsById(CATEGORY_ID)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> supplierService.delete(CATEGORY_ID))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Supplier not found with id: " + CATEGORY_ID);
        }

        @Test
        void existsById_shouldReturnTrueWhenSupplierExists() {
            // Arrange
            when(supplierRepository.existsById(CATEGORY_ID)).thenReturn(true);

            // Act
            boolean result = supplierService.existsById(CATEGORY_ID);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        void existsById_shouldReturnFalseWhenSupplierNotExists() {
            // Arrange
            when(supplierRepository.existsById(CATEGORY_ID)).thenReturn(false);

            // Act
            boolean result = supplierService.existsById(CATEGORY_ID);

            // Assert
            assertThat(result).isFalse();
        }

}
