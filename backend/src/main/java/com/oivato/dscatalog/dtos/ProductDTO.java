//package com.oivato.dscatalog.dtos;
//
//import com.oivato.dscatalog.entities.Product;
//
//import java.io.Serializable;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProductDTO implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    private Long id;
//    private String name;
//    private String description;
//    private Double price;
//    private String imageUrl;
//    private Instant date;
//    private List<CategoryDTO> categories = new ArrayList<>();
//
//    public ProductDTO() {
//    }
//
//    public ProductDTO(Long id, String name, String description, Double price, String imageUrl, Instant date) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.imageUrl = imageUrl;
//        this.date = date;
//    }
//
//    public ProductDTO(Product product) {
//
//    }
//}
