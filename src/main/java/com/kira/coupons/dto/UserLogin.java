package com.kira.coupons.dto;


public class UserLogin {
    private int id;
    private String userType;
    private Integer companyId;

    public UserLogin(int id, String userType, Integer companyId) {
        this.id = id;
        this.userType = userType;
        this.companyId = companyId;
    }

    public UserLogin() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "id=" + id +
                ", userType='" + userType + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
