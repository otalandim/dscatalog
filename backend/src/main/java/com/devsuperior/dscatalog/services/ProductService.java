package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String PRODUCT_NOT_FOUND = "Product not found";
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> list = productRepository.findAll(pageRequest);
        return list.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product product = obj.orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO create(ProductDTO productDto) {
        Product product = new Product();
        copyDtoToEntity(productDto, product);
        product = productRepository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDto) {
        try {
            Product product = productRepository.getOne(id);
            copyDtoToEntity(productDto, product);
            product = productRepository.save(product);
            return new ProductDTO(product);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
        } catch (DataIntegrityViolationException d) {
            throw new DatabaseException(INTERNAL_SERVER_ERROR);
        }
    }

    private void copyDtoToEntity(ProductDTO productDto, Product product) {
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setDate(productDto.getDate());
        product.setImgUrl(productDto.getImgUrl());
        product.setPrice(productDto.getPrice());
        product.getCategories().clear();

        for (CategoryDTO categoryDto : productDto.getCategories()) {
            Category category = categoryRepository.getOne(categoryDto.getId());
            product.getCategories().add(category);
        }
    }
}
