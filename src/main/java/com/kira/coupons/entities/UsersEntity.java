package com.kira.coupons.entities;

import javax.persistence.*;

@Entity
@Table (name = "users")
public class UsersEntity {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column (name = "password",  nullable = false)
    private String password;
    @Column (name = "user_type", nullable = false)
    private String userType;
    @ManyToOne
    private CompaniesEntity company;

    public UsersEntity() {
    }

    public UsersEntity(int id, String username, String password, String userType, Integer companyId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
        if (userType.equals("company")) {
            this.company = new CompaniesEntity();
            this.company.setId(companyId);
        }
    }

    public UsersEntity(String username, String password, String userType, Integer companyId) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        if (userType.equals("company")) {
            this.company = new CompaniesEntity();
            this.company.setId(companyId);
        }
    }

    public CompaniesEntity getCompany() {
        return company;
    }

    public void setCompany(CompaniesEntity company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
