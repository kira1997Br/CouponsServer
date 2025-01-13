package com.kira.coupons.dal;

import com.kira.coupons.dto.Coupon;
import com.kira.coupons.dto.CouponDetails;
import com.kira.coupons.entities.CouponsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository
public interface ICouponsDal extends JpaRepository<CouponsEntity, Integer> {

    @Query("SELECT new com.kira.coupons.dto.CouponDetails(c.id, c.title, c.description," +
            "c.price, c.startDate, c.endDate, c.amount, c.imageUrl, c.company.name," +
            "c.category.name) FROM CouponsEntity c WHERE c.company.name = :companyName")
    Page<CouponDetails> findCouponsByCompanyName(Pageable pageable, @Param("companyName") String companyName);

    @Query("SELECT new com.kira.coupons.dto.Coupon(c.id, c.title, c.description," +
            "c.price, c.company.id, c.category.id, c.startDate, c.endDate, c.amount," +
            "c.imageUrl) FROM CouponsEntity c WHERE c.company.id = :companyId")
    Page<Coupon> findCouponsByCompanyId(Pageable pageable, @Param("companyId") int companyId);

    @Query("SELECT new com.kira.coupons.dto.CouponDetails(c.id, c.title, c.description," +
            "c.price, c.startDate, c.endDate, c.amount, c.imageUrl, c.company.name," +
            "c.category.name) FROM CouponsEntity c WHERE c.category.name = :categoryName")
    Page<CouponDetails> findCouponsByCategoryName(Pageable pageable, @Param("categoryName") String categoryName);

    @Query("SELECT new com.kira.coupons.dto.CouponDetails(c.id, c.title, c.description," +
            "c.price, c.startDate, c.endDate, c.amount, c.imageUrl, c.company.name," +
            "c.category.name) FROM CouponsEntity c WHERE c.title = :title")
    Page<CouponDetails> findCouponsByTitle(Pageable pageable, @Param("title") String title);

    @Query("SELECT new com.kira.coupons.dto.CouponDetails(c.id, c.title, c.description," +
            "c.price, c.startDate, c.endDate, c.amount, c.imageUrl, c.company.name," +
            "c.category.name) from CouponsEntity c")
    Page<CouponDetails> getListOfCoupons(Pageable pageable);

    @Query("SELECT new com.kira.coupons.dto.CouponDetails(c.id, c.title, c.description," +
            "c.price, c.startDate, c.endDate, c.amount, c.imageUrl, c.company.name," +
            "c.category.name) from CouponsEntity c WHERE c.id=:id")
    CouponDetails getCouponDetailsBy (@Param("id") int couponId);


    @Modifying
    @Transactional
    @Query("DELETE FROM CouponsEntity c WHERE endDate < :currentDate")
    void deleteExpiredCoupons(@Param("currentDate") Date currentDate);
}
