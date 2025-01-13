package com.kira.coupons.entities;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.Date;

@Entity
@Table(name = "purchases")

public class PurchasesEntity {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private CustomersEntity customer;
    @ManyToOne
    private CouponsEntity coupon;
    @Column(name = "amount", nullable = false)
    private int amount;
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    public PurchasesEntity() {
    }

    public PurchasesEntity(int id, int customerId, int couponId, int amount, Date timestamp) {
        this.id = id;
        this.customer = new CustomersEntity();
        this.customer.setId(customerId);
        this.coupon = new CouponsEntity();
        this.coupon.setId(couponId);
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public PurchasesEntity(int customerId, int couponId, int amount, Date timestamp) {
        this.customer = new CustomersEntity();
        this.customer.setId(customerId);
        this.coupon = new CouponsEntity();
        this.coupon.setId(couponId);
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public CustomersEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomersEntity customer) {
        this.customer = customer;
    }

    public CouponsEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponsEntity coupon) {
        this.coupon = coupon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
