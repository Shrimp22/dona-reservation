package com.example.models.request;

public class ChangePasswordRequest {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public ChangePasswordRequest() {}


    public ChangePasswordRequest(String password) {
        this.setPassword(password);
    }
}
