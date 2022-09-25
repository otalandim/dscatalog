package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private long existingId;
    private long noExistingId;
    private long dependId;

    @BeforeEach
    public void setup() {
        existingId = 1L;
        noExistingId = 1000L;
        dependId = 4L;

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(noExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependId);
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
