package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.utils.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long noExistingId;
    private long dependId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductDTO productDto;

    @BeforeEach
    public void setup() {
        existingId = 1L;
        noExistingId = 1000L;
        dependId = 4L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        productDto = Factory.createProductDto();
        page = new PageImpl<>(List.of(product));

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(noExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.getOne(existingId)).thenReturn(product);
        Mockito.when(repository.getOne(noExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(noExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(noExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependId);
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExist(){
        ProductDTO res = service.findById(existingId);
        Assertions.assertNotNull(res);
        Assertions.assertEquals(existingId,res.getId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(noExistingId);
        });
    }

    @Test
    public void findAllPageShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExist(){
        ProductDTO res = service.update(existingId,productDto);
        Assertions.assertNotNull(res);
        Assertions.assertEquals(existingId,res.getId());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(noExistingId, productDto);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowEmptyResourceNotFoundExceptionWhenIdDoesNotExist () {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(noExistingId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(noExistingId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExist () {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(dependId);
    }
}
