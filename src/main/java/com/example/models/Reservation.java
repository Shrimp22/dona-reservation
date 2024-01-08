package com.example.models;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Boolean approved = false;

    private Boolean termTaken = false;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Product.class)
    @JoinColumn(name="product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Transient
    private Long productId;

    public Reservation() {}

    public Reservation(LocalDateTime startDate, LocalDateTime endDate, Product product, User user, boolean approved, boolean termTaken) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.product = product;
        this.user = user;
        this.approved = approved;
        this.termTaken = termTaken;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void approve() {
        this.approved = true;
    }

    public void deny() {
        this.approved = false;
    }

    public boolean getTerm() {
        return termTaken;
    }

    public void setTerm(boolean termTaken) {
        this.termTaken = termTaken;
    }
}