package com.example.dto;

import com.example.models.Product;

public class ProductUserDto extends ProductDto {
    private UserDto user;

    public ProductUserDto(Product p) {
        super(p);
        this.user = new UserDto(p.getUser());
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
