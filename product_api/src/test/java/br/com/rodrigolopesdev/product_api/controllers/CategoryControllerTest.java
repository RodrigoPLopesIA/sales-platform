package br.com.rodrigolopesdev.product_api.controllers;

import br.com.rodrigolopesdev.product_api.dtos.ListCategoryDTO;
import br.com.rodrigolopesdev.product_api.dtos.SaveCategoryDTO;
import br.com.rodrigolopesdev.product_api.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/v1/categories";
    private final UUID CATEGORY_ID = UUID.fromString("85260ea5-daad-4bb2-90fe-ee7e69e39d42");
    private final String CATEGORY_NAME = "Electronics";
    private final SaveCategoryDTO validDto = new SaveCategoryDTO(CATEGORY_NAME);
    private final ListCategoryDTO listDto = new ListCategoryDTO(CATEGORY_ID, CATEGORY_NAME);


    @Test
    void createCategory_shouldReturn201WhenValid() throws Exception {
        // Arrange
        given(categoryService.create(any(SaveCategoryDTO.class))).willReturn(listDto);

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(CATEGORY_ID.toString()))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME.toString()));
    }

    @Test
    void createCategory_shouldValidateNullName() throws Exception {
        // Arrange
        SaveCategoryDTO invalidDto = new SaveCategoryDTO(null);

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Arguments invalid!"))
                .andExpect(jsonPath("$.errors.name").value("must not be blank"));
    }

    @Test
    void createCategory_shouldValidateBlankName() throws Exception {
        // Arrange
        SaveCategoryDTO invalidDto = new SaveCategoryDTO("   ");

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Arguments invalid!"))
                .andExpect(jsonPath("$.errors.name").value("must not be blank"));
    }

    @Test
    void getCategory_shouldReturn200WhenExists() throws Exception {
        // Arrange
        given(categoryService.findById(CATEGORY_ID)).willReturn(listDto);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/{id}", CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID.toString()))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME.toString()));
    }

    @Test
    void getCategory_shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        given(categoryService.findById(CATEGORY_ID))
                .willThrow(new EntityNotFoundException("Category not found with id " + CATEGORY_ID));

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/{id}", CATEGORY_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Category not found with id " + CATEGORY_ID));
    }

    /* Testes para PUT /api/v1/categories/{id} */
    @Test
    void updateCategory_shouldReturn200WhenValid() throws Exception {
        // Arrange
        given(categoryService.update(eq(CATEGORY_ID), any(SaveCategoryDTO.class)))
                .willReturn(listDto);

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID.toString()))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME.toString()));
    }

    @Test
    void updateCategory_shouldValidateNullName() throws Exception {
        // Arrange
        SaveCategoryDTO invalidDto = new SaveCategoryDTO(null);

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Arguments invalid!"))
                .andExpect(jsonPath("$.errors.name").value("must not be blank"));
    }

    @Test
    void deleteCategory_shouldReturn204WhenSuccess() throws Exception {
        // Arrange
        willDoNothing().given(categoryService).delete(CATEGORY_ID);

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", CATEGORY_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCategory_shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        willThrow(new EntityNotFoundException("Category not found with id: " + CATEGORY_ID))
                .given(categoryService).delete(CATEGORY_ID);

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", CATEGORY_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Category not found with id: " + CATEGORY_ID));
    }


    @Test
    void existsById_shouldReturn200WithTrueWhenExists() throws Exception {
        // Arrange
        given(categoryService.existsById(CATEGORY_ID)).willReturn(true);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/exists/{id}", CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsById_shouldReturn200WithFalseWhenNotExists() throws Exception {
        // Arrange
        given(categoryService.existsById(CATEGORY_ID)).willReturn(false);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/exists/{id}", CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}