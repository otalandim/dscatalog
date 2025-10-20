//package com.oivato.dscatalog.entities;
//
//import com.oivato.dscatalog.dtos.CategoryDTO;
//
//import java.io.Serializable;
//import java.time.Instant;
//import java.util.*;
//
//public class Product implements Serializable {
//    private static final long serialVersionID = 1L;
//
//    private Long id;
//    private String name;
//    private String description;
//    private Double price;
//    private String imageUrl;
//    private Instant date;
//    private Set<Category> categories = new HashSet<>();
//
//    public Product(){}
//
//    public Product(Long id, String name, String description, Double price, String imageUrl, Instant date) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.imageUrl = imageUrl;
//        this.date = date;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Double getPrice() {
//        return price;
//    }
//
//    public void setPrice(Double price) {
//        this.price = price;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public Instant getDate() {
//        return date;
//    }
//
//    public void setDate(Instant date) {
//        this.date = date;
//    }
//
//    public Set<Category> getCategories() {
//        return categories;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        Product product = (Product) o;
//        return Objects.equals(id, product.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(id);
//    }
//}
