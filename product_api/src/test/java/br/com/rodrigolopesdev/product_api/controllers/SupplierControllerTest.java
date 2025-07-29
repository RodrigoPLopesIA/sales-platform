package br.com.rodrigolopesdev.product_api.controllers;

import br.com.rodrigolopesdev.product_api.dtos.ListSupplierDTO;
import br.com.rodrigolopesdev.product_api.dtos.SaveSupplierDTO;
import br.com.rodrigolopesdev.product_api.services.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupplierController.class)
@ExtendWith(MockitoExtension.class)
class SupplierControllerTest {

    @MockitoBean
    private SupplierService supplierService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/v1/suppliers";
    private final UUID CATEGORY_ID = UUID.fromString("85260ea5-daad-4bb2-90fe-ee7e69e39d42");
    private final String CATEGORY_NAME = "Electronics";
    private final SaveSupplierDTO validDto = new SaveSupplierDTO(CATEGORY_NAME);
    private final ListSupplierDTO listDto = new ListSupplierDTO(CATEGORY_ID, CATEGORY_NAME);


    @Test
    void createSupplier_shouldReturn201WhenValid() throws Exception {
        // Arrange
        given(supplierService.create(any(SaveSupplierDTO.class))).willReturn(listDto);

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
    void createSupplier_shouldValidateNullName() throws Exception {
        // Arrange
        SaveSupplierDTO invalidDto = new SaveSupplierDTO(null);

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Arguments invalid!"))
                .andExpect(jsonPath("$.errors.name").value("must not be blank"));
    }

    @Test
    void createSupplier_shouldValidateBlankName() throws Exception {
        // Arrange
        SaveSupplierDTO invalidDto = new SaveSupplierDTO("   ");

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Arguments invalid!"))
                .andExpect(jsonPath("$.errors.name").value("must not be blank"));
    }

    @Test
    void getSupplier_shouldReturn200WhenExists() throws Exception {
        // Arrange
        given(supplierService.findById(CATEGORY_ID)).willReturn(listDto);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/{id}", CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID.toString()))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME.toString()));
    }

    @Test
    void getSupplier_shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        given(supplierService.findById(CATEGORY_ID))
                .willThrow(new EntityNotFoundException("Supplier not found with id " + CATEGORY_ID));

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/{id}", CATEGORY_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Supplier not found with id " + CATEGORY_ID));
    }

    /* Testes para PUT /api/v1/suppliers/{id} */
    @Test
    void updateSupplier_shouldReturn200WhenValid() throws Exception {
        // Arrange
        given(supplierService.update(eq(CATEGORY_ID), any(SaveSupplierDTO.class)))
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
    void updateSupplier_shouldValidateNullName() throws Exception {
        // Arrange
        SaveSupplierDTO invalidDto = new SaveSupplierDTO(null);

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Arguments invalid!"))
                .andExpect(jsonPath("$.errors.name").value("must not be blank"));
    }

    @Test
    void deleteSupplier_shouldReturn204WhenSuccess() throws Exception {
        // Arrange
        willDoNothing().given(supplierService).delete(CATEGORY_ID);

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", CATEGORY_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteSupplier_shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        willThrow(new EntityNotFoundException("Supplier not found with id: " + CATEGORY_ID))
                .given(supplierService).delete(CATEGORY_ID);

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", CATEGORY_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Supplier not found with id: " + CATEGORY_ID));
    }


    @Test
    void existsById_shouldReturn200WithTrueWhenExists() throws Exception {
        // Arrange
        given(supplierService.existsById(CATEGORY_ID)).willReturn(true);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/exists/{id}", CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsById_shouldReturn200WithFalseWhenNotExists() throws Exception {
        // Arrange
        given(supplierService.existsById(CATEGORY_ID)).willReturn(false);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/exists/{id}", CATEGORY_ID))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}