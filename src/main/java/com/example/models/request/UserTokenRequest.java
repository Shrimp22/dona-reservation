package com.example.models.request;

public class UserTokenRequest {

    private String email;


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public UserTokenRequest() {}

    public UserTokenRequest(String email) {
        this.setEmail(email);
    }
    
}
