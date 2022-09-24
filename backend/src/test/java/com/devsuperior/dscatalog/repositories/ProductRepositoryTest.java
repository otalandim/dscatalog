package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.utils.Factory;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long noExistingId;
    private long totalProducts;

    @BeforeEach
    public void setup() {
        existingId = 1L;
        noExistingId = 1000L;
        totalProducts = 25L;
    }

    @Test
    public void saveShouldInsertObjectWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        assertNotNull(product.getId());
        assertEquals(totalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldSearchByObjectWhenIdExists() {
        Optional<Product> res = repository.findById(existingId);
        assertEquals(1L, res.get().getId());
        assertTrue(res.isPresent());
    }

    @Test
    public void findByIdShouldSearchByObjectWhenIdDoesNotExists() {
        Optional<Product> res = repository.findById(noExistingId);
        assertTrue(res.isEmpty());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existingId);

        Optional<Product> res = repository.findById(existingId);
        assertFalse(res.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){
        assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(noExistingId);
        });
    }
}
