package com.kira.coupons.logic;

import com.kira.coupons.dal.ICouponsDal;
import com.kira.coupons.dal.IPurchasesDal;
import com.kira.coupons.dto.Coupon;
import com.kira.coupons.dto.Purchase;
import com.kira.coupons.dto.PurchaseDetails;
import com.kira.coupons.entities.PurchasesEntity;
import com.kira.coupons.enums.ErrorType;
import com.kira.coupons.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PurchasesLogic {
    private IPurchasesDal purchasesDal;
    private CouponsLogic couponsLogic;
    private CustomersLogic customersLogic;
    private ICouponsDal couponsDal;

    @Autowired
    public PurchasesLogic(IPurchasesDal purchasesDal, CouponsLogic couponsLogic, CustomersLogic customersLogic, ICouponsDal couponsDal) {
        this.purchasesDal = purchasesDal;
        this.couponsLogic = couponsLogic;
        this.customersLogic = customersLogic;
        this.couponsDal = couponsDal;
    }

    @Transactional
    public void createPurchase(Purchase purchase, String userType) throws ServerException, RuntimeException {
        if (!userType.equals("customer")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        validatePurchase(purchase);
        PurchasesEntity purchasesEntity = convertPurchaseToPurchaseEntity(purchase);
        this.purchasesDal.save(purchasesEntity);
        this.couponsLogic.decreaseCouponAmount(purchase.getAmount(), purchase.getCouponId());
    }


    public PurchaseDetails getPurchaseDetailsByPurchaseId(int purchaseId, int userId) throws ServerException {
        PurchasesEntity purchaseEntity = purchasesDal.findById(purchaseId).get();
        if (purchaseEntity.getCustomer().getId() != userId) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        return purchasesDal.getPurchaseDetailsById(purchaseId);
    }


    public Page<PurchaseDetails> getPurchaseDetailsByCustomer(int page, int size,int customerId) throws ServerException {
        Pageable pageable = PageRequest.of(page, size);
        return purchasesDal.getPurchasesByCustomerId(pageable,customerId);
    }

    public Page<PurchaseDetails> getPurchasesByAdmin(int page, int size,String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        Pageable pageable = PageRequest.of(page, size);
        return purchasesDal.getPurchases(pageable);
    }

    @Transactional
    public void deletePurchase(int purchaseId, String userType) throws ServerException, RuntimeException {
        Purchase purchase = getPurchaseById(purchaseId);
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        this.couponsLogic.increaseCouponAmount(purchase.getAmount(), purchase.getCouponId());
        this.purchasesDal.deleteById(purchaseId);
    }

    private Purchase getPurchaseById (int purchaseId){
        PurchasesEntity purchasesEntity= this.purchasesDal.findById(purchaseId).get();
        Purchase purchase = convertPurchaseEntityToPurchase (purchasesEntity);
        return purchase;
    }

    private Purchase convertPurchaseEntityToPurchase(PurchasesEntity purchasesEntity) {
        Purchase purchase = new Purchase(purchasesEntity.getId(),
                purchasesEntity.getCustomer().getId(),
                purchasesEntity.getCoupon().getId(),
                purchasesEntity.getAmount(),
                purchasesEntity.getTimestamp());
        return purchase;
    }


    private PurchasesEntity convertPurchaseToPurchaseEntity(Purchase purchase) {
        PurchasesEntity purchasesEntity = new PurchasesEntity(purchase.getId(),
                purchase.getCustomerId(),
                purchase.getCouponId(),
                purchase.getAmount(),
                purchase.getTimestamp());
        return purchasesEntity;
    }


    private void validatePurchase(Purchase purchase) throws ServerException {
        Coupon coupon = couponsLogic.getCouponByIdForInternal(purchase.getCouponId());

        if (purchase.getAmount() < 1) {
            throw new ServerException(ErrorType.INVALID_AMOUNT_OF_PURCHASES);
        }
        if (coupon == null) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        if (purchase.getAmount() > coupon.getAmount()) {
            throw new ServerException(ErrorType.INVALID_AMOUNT_OF_COUPONS);
        }
        if (purchase.getTimestamp().after(coupon.getEndDate())) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
    }


}
