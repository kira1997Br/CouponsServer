package com.kira.coupons.dto;

import java.sql.Timestamp;
import java.util.Date;

public class Purchase {
    private int id;
    private int customerId;
    private int couponId;
    private int amount;
    private Date timestamp;

    public Purchase() {
    }

    public Purchase(int id, int customer_id, int coupon_id, int amount, Date timestamp) {
        this.id = id;
        this.customerId = customer_id;
        this.couponId = coupon_id;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Purchase(int customer_id, int coupon_id, int amount, Date timestamp) {
        this.customerId = customer_id;
        this.couponId = coupon_id;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", couponId=" + couponId +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
