package com.kira.coupons.dto;

import java.util.Date;
import java.sql.Timestamp;

public class PurchaseDetails {
    private int id;
    private int couponId;
    private int customerId;
    private Date timestamp;
    private float totalPrice;
    private String title;
    private String  description;
    private int amount;
    private Date endDate;

    public PurchaseDetails(int id, int couponId, int customerId, Date timestamp, float totalPrice, String title, String description, int amount, Date endDate) {
        this.id = id;
        this.couponId = couponId;
        this.customerId = customerId;
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.endDate = endDate;
    }

    public PurchaseDetails() {
    }

    public PurchaseDetails(int couponId, int customerId, Date timestamp, float totalPrice, String title, String description, int amount, Date endDate) {
        this.couponId = couponId;
        this.customerId = customerId;
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.endDate = endDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }


    public int getCouponId() {
        return couponId;
    }

    public int getCustomerId() {
        return customerId;
    }


    public Date getTimestamp() {
        return timestamp;
    }


    public float getTotalPrice() {
        return totalPrice;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PurchaseDetails{" +
                "id=" + id +
                ", couponId=" + couponId +
                ", customerId=" + customerId +
                ", timestamp=" + timestamp +
                ", totalPrice=" + totalPrice +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", endDate=" + endDate +
                '}';
    }
}
