package com.example.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "user_token")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime exprTime;

    @Transient
    private Long userId;

    @OneToOne
    private User user;


    public UserToken() {}


    public UserToken(Long id, String token, User user, LocalDateTime expTime) {
        this.setId(id);
        this.setToken(token);
        this.setUser(user);
        this.setExprTime(expTime);   
    }

    public Long getUserId() {
        return userId;
    }

    public void setExprTime(LocalDateTime exprTime) {
        this.exprTime = exprTime;
    }
    public LocalDateTime getExprTime() {
        return exprTime;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public void setToken(String token) {
        this.token = token;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }
    public String getToken() {
        return token;
    }
    public User getUser() {
        return user;
    }


    
}
