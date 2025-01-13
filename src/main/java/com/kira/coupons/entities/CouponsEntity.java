package com.kira.coupons.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "coupons")
public class CouponsEntity {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "price", nullable = false)
    private float price;
    @ManyToOne
    private CompaniesEntity company;
    @ManyToOne
    private CategoriesEntity category;
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @Column(name = "end_date", nullable = false)
    private Date endDate;
    @Column(name = "amount", nullable = false)
    private int amount;
    @Column(name = "image_url")
    private String imageUrl;
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<PurchasesEntity> purchases;

    public CouponsEntity() {
    }

    public CouponsEntity(int id, String title, String description, float price, int companyId, int categoryId, Date startDate, Date endDate, int amount, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.company = new CompaniesEntity();
        this.company.setId(companyId);
        this.category = new CategoriesEntity();
        this.category.setId(categoryId);
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.imageUrl = imageUrl;
    }

    public CouponsEntity(String title, String description, float price, int companyId, int categoryId, Date startDate, Date endDate, int amount, String imageUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.company = new CompaniesEntity();
        this.company.setId(companyId);
        this.category = new CategoriesEntity();
        this.category.setId(categoryId);
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.imageUrl = imageUrl;
    }

    public List<PurchasesEntity> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchasesEntity> purchases) {
        this.purchases = purchases;
    }

    public CompaniesEntity getCompany() {
        return company;
    }

    public void setCompany(CompaniesEntity company) {
        this.company = company;
    }

    public CategoriesEntity getCategory() {
        return category;
    }

    public void setCategory(CategoriesEntity category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
