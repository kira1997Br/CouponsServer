package com.kira.coupons.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="categories")
public class CategoriesEntity {
    @Id
    @GeneratedValue
    private int id;

    @Column(name="name", nullable = false)
    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CouponsEntity> coupons;

    public CategoriesEntity() {
    }

    public CategoriesEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoriesEntity(String name) {
        this.name = name;
    }

    public List<CouponsEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponsEntity> coupons) {
        this.coupons = coupons;
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
}
