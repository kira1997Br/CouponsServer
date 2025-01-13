package com.kira.coupons.dto;


public class UserLoginData {
    private String username;
    private String password;

    public UserLoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserLoginData() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

