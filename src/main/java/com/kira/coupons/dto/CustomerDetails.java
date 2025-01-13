package com.kira.coupons.dto;

public class CustomerDetails {
    private int id;
    private String name;
    private String userName;
    private String address;
    private String phone;

    public CustomerDetails() {

    }

    public CustomerDetails(String name, String userName, String address, String phone) {
        this.name = name;
        this.userName = userName;
        this.address = address;
        this.phone = phone;
    }

    public CustomerDetails(int id, String name, String userName, String address, String phone) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.address = address;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "CustomerDetails{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
