package com.example.dto;

import com.example.models.Product;
import com.example.models.Reservation;

import java.time.LocalDateTime;

public class UserReservations {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProductDto product;

    private UserDto user;

    public UserReservations(Reservation r) {
        this.id = r.getId();
        this.startDate = r.getStartDate();
        this.endDate = r.getEndDate();
        this.product = new ProductDto(r.getProduct());
        this.user = new UserDto(r.getUser());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
