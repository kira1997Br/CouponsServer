package com.kira.coupons.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
public class CustomersEntity {
    @Id
    private int id;
    @OneToOne(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private UsersEntity user;
    @Column (name = "name",  nullable = false)
    private String name;
    @Column (name = "address",  nullable = false)
    private String address;
    @Column (name = "phone",  nullable = false)
    private String phone;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<PurchasesEntity> purchases;

    public CustomersEntity() {
    }


    public CustomersEntity(int id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public CustomersEntity(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public List<PurchasesEntity> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchasesEntity> purchases) {
        this.purchases = purchases;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
