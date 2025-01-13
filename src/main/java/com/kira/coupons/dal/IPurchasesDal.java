package com.kira.coupons.dal;

import com.kira.coupons.dto.PurchaseDetails;
import com.kira.coupons.entities.PurchasesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPurchasesDal extends JpaRepository<PurchasesEntity, Integer> {
    @Query("SELECT new com.kira.coupons.dto.PurchaseDetails(p.id, p.coupon.id, p.customer.id, p.timestamp, (p.amount * p.coupon.price) AS totalPrice, " +
            "p.coupon.title, p.coupon.description,  p.amount, p.coupon.endDate) " +
            "FROM PurchasesEntity p " +
            "WHERE p.id = :id")
    PurchaseDetails getPurchaseDetailsById(@Param("id") int id);

    @Query("SELECT new com.kira.coupons.dto.PurchaseDetails(p.id, p.coupon.id, p.customer.id, p.timestamp, (p.amount * p.coupon.price) AS totalPrice, " +
            "p.coupon.title, p.coupon.description, p.amount, p.coupon.endDate) " +
            "FROM PurchasesEntity p " +
            "WHERE p.customer.id = :customerId")
    Page<PurchaseDetails> getPurchasesByCustomerId(Pageable pageable, @Param("customerId") int customerId);

    @Query("SELECT new com.kira.coupons.dto.PurchaseDetails(p.id, p.coupon.id, p.customer.id, p.timestamp, (p.amount * p.coupon.price) AS totalPrice, " +
            "p.coupon.title, p.coupon.description, p.amount, p.coupon.endDate) " +
            "FROM PurchasesEntity p ")
    Page<PurchaseDetails> getPurchases(Pageable pageable);
}
