package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.utils.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long noExistingId;
    private Long dependentId;
    private ProductDTO productDto;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    public void setup(){
        existingId = 1L;
        noExistingId = 2L;
        dependentId = 3L;
        productDto = Factory.createProductDto();
        page = new PageImpl<>(List.of(productDto));

        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDto);
        when(service.findById(noExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.create(any())).thenReturn(productDto);

        when(service.update(eq(existingId), any())).thenReturn(productDto);
        when(service.update(eq(noExistingId), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(noExistingId);
        doThrow(DatabaseException.class).when(service).delete(dependentId);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc
            .perform(
                get("/products")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()
            );
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExist() throws Exception {
        mockMvc
            .perform(
                get("/products/{id}", existingId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").exists()
            );
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc
            .perform(
                get("/products/{id}", noExistingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()
            );
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDto);

        mockMvc
            .perform(
                put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists()
            );
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDto);

        mockMvc
            .perform(
                put("/products/{id}", noExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()
            );
    }


    @Test
    public void insertShouldReturnProductDto() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDto);

        mockMvc
            .perform(
                post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()
            );
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExist() throws Exception {
        mockMvc
            .perform(
                delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()
            );
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdDoesNotExist() throws Exception {
        mockMvc
            .perform(
                delete("/products/{id}", noExistingId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()
            );
    }
}
