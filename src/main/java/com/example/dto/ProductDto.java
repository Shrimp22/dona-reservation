package com.example.dto;

import com.example.models.Product;

public class ProductDto {
    private Long id;
    private String name;
    private float price;

    public ProductDto() {}

    public ProductDto(Product p) {
        this.id = p.getId();
        this.name = p.getName();
        this.price = p.getPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
