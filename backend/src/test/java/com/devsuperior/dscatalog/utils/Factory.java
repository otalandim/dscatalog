package com.devsuperior.dscatalog.utils;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(
                1L,
                "O Fim da Ansiedade",
                "Livro que fala sobre a ansiedade",
                40.0,
                "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg",
                Instant.parse("2020-07-13T20:50:07.12345Z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDto(){
        Product prod = createProduct();
        return new ProductDTO(prod, prod.getCategories());
    }

    public static Category createCategory() {
        return new Category(1L, "Books");
    }
}
