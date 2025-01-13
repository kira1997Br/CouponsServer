package com.kira.coupons.logic;

import com.kira.coupons.dal.ICouponsDal;
import com.kira.coupons.dto.Coupon;
import com.kira.coupons.dto.CouponDetails;
import com.kira.coupons.dto.UserLogin;
import com.kira.coupons.entities.CouponsEntity;
import com.kira.coupons.enums.ErrorType;
import com.kira.coupons.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CouponsLogic {
    private ICouponsDal couponsDal;
    private CategoriesLogic categoriesLogic;

    @Autowired
    public CouponsLogic(ICouponsDal couponsDal, CategoriesLogic categoriesLogic) {
        this.couponsDal = couponsDal;
        this.categoriesLogic = categoriesLogic;
    }

    @Transactional
    public void createCouponByCompany(Coupon coupon, UserLogin userLogin) throws ServerException, RuntimeException {
        if (!userLogin.getUserType().equals("company")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        validateCoupon(coupon);
        coupon.setCompanyId(userLogin.getCompanyId());
        CouponsEntity couponsEntity = convertCouponToCouponsEntity(coupon);
        this.couponsDal.save(couponsEntity);
    }

    @Transactional
    public void createCouponByAdmin (Coupon coupon, String userType)throws Exception, RuntimeException{
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        validateCoupon(coupon);
        CouponsEntity couponsEntity = convertCouponToCouponsEntity(coupon);
        this.couponsDal.save(couponsEntity);
    }


    public void updateCouponByCompany(Coupon coupon, UserLogin userLogin) throws ServerException {
        if (!userLogin.getUserType().equals("company")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        CouponsEntity couponsEntity = this.couponsDal.findById(coupon.getId()).get();
        if (couponsEntity == null) {
            throw new ServerException(ErrorType.INVALID_COUPON_ID);
        }

        couponsEntity.setDescription(coupon.getDescription());
        couponsEntity.setTitle(coupon.getTitle());
        couponsEntity.setAmount(coupon.getAmount());
        couponsEntity.setPrice(coupon.getPrice());
        couponsEntity.setEndDate(coupon.getEndDate());
        couponsEntity.setStartDate(coupon.getStartDate());
        couponsEntity.setImageUrl(coupon.getImageUrl());
        validateCoupon(coupon);

        this.couponsDal.save(couponsEntity);
    }

    public void updateCouponByAdmin(Coupon coupon, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        CouponsEntity couponsEntity = this.couponsDal.findById(coupon.getId()).get();
        if (couponsEntity == null) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        couponsEntity.setDescription(coupon.getDescription());
        couponsEntity.setTitle(coupon.getTitle());
        couponsEntity.setAmount(coupon.getAmount());
        couponsEntity.setPrice(coupon.getPrice());
        couponsEntity.setEndDate(coupon.getEndDate());
        couponsEntity.setStartDate(coupon.getStartDate());
        //couponEntity.setCategory(coupon.getCategoryId());
        couponsEntity.setImageUrl(coupon.getImageUrl());
        validateCoupon(coupon);

        this.couponsDal.save(couponsEntity);

    }


    @Transactional
    public void deleteCouponByAdmin(int id, String userType) throws ServerException, RuntimeException {
        if (!userType.equals("Admin")) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }

        this.couponsDal.deleteById(id);
    }

    @Transactional
    public void deleteCouponByCompany(int id, UserLogin userLogin) throws ServerException, RuntimeException {
        if(!userLogin.getUserType().equals("Company")){
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        CouponsEntity couponEntity = couponsDal.findById(id).get();

        if(couponEntity.getCompany().getId() != userLogin.getCompanyId()){
            throw new ServerException(ErrorType.GENERAL_ERROR,
                    "You are trying to delete a coupon that does not belong to your company");
        }

        this.couponsDal.deleteById(id);
    }


    public Page<CouponDetails> getCouponsByCompanyName (int page, int size,String companyName){
        Pageable pageable = PageRequest.of(page , size);
        return this.couponsDal.findCouponsByCompanyName(pageable,companyName);
    }

    public Page<CouponDetails> getCouponsByCategoryName(int page, int size, String categoryName) {
        Pageable pageable = PageRequest.of(page , size);
        return this.couponsDal.findCouponsByCategoryName(pageable,categoryName);
    }

    public Page<Coupon> getCouponsByCompanyId(int page, int size, int companyId) {
        Pageable pageable = PageRequest.of(page , size);
        return this.couponsDal.findCouponsByCompanyId(pageable,companyId);
    }



    public Page<CouponDetails> getListOfCoupons(int page, int size) {
        Pageable pageable = PageRequest.of(page , size);
        return this.couponsDal.getListOfCoupons(pageable);
    }

    public Page<CouponDetails> getListOfCouponsByTitle(int page, int size,String title)  {
        Pageable pageable = PageRequest.of(page , size);
       return this.couponsDal.findCouponsByTitle(pageable,title);

    }

    public Coupon getCouponById(int id, String userType) throws ServerException {
        if (!userType.equals("admin")){
            throw new ServerException(ErrorType.UNAUTHORIZED);
        }
        if (!this.couponsDal.existsById(id)){
            throw new ServerException(ErrorType.INVALID_COMPANY_ID);
        }
        CouponsEntity couponEntity = this.couponsDal.findById(id).get();
        Coupon coupon = convertCouponEntityToCoupon(couponEntity);
        return coupon;
    }

    public CouponDetails getCouponDetailsById (int id) throws ServerException{
       return this.couponsDal.getCouponDetailsBy(id);
    }

    public Coupon getCouponByIdForInternal(int id) throws ServerException {
        return getInternalCouponById(id);
    }

    // Protected method with core logic (for inheritance/same package)
    protected Coupon getInternalCouponById(int id) throws ServerException {
        if (!this.couponsDal.existsById(id)) {
            throw new ServerException(ErrorType.INVALID_COMPANY_ID);
        }
        CouponsEntity couponEntity = couponsDal.findById(id).get();
        return convertCouponEntityToCoupon(couponEntity);
}

    private Coupon convertCouponEntityToCoupon(CouponsEntity couponEntity) {
        Coupon coupon = new Coupon(
                couponEntity.getId(),
                couponEntity.getTitle(),
                couponEntity.getDescription(),
                couponEntity.getPrice(),
                couponEntity.getCompany().getId(),
                couponEntity.getCategory().getId(),
                couponEntity.getStartDate(),
                couponEntity.getEndDate(),
                couponEntity.getAmount(),
                couponEntity.getImageUrl());
        return coupon;
    }

    private CouponDetails convertCouponEntityToCouponDetails(CouponsEntity couponEntity) {
        CouponDetails coupon = new CouponDetails(
                couponEntity.getId(),
                couponEntity.getTitle(),
                couponEntity.getDescription(),
                couponEntity.getPrice(),
                couponEntity.getStartDate(),
                couponEntity.getEndDate(),
                couponEntity.getAmount(),
                couponEntity.getImageUrl(),
                couponEntity.getCompany().getName(),
                couponEntity.getCategory().getName()
        );
        return coupon;
    }

    private List<CouponDetails> convertCouponsEntitiesToCouponsList(List<CouponsEntity> couponsEntities) {
        List<CouponDetails> coupons = new ArrayList<>();
        for (CouponsEntity couponsEntity : couponsEntities) {
            CouponDetails coupon = convertCouponEntityToCouponDetails(couponsEntity);
            coupons.add(coupon);
        }
        return coupons;
    }

    private CouponsEntity convertCouponToCouponsEntity(Coupon coupon) {
        CouponsEntity couponsEntity = new CouponsEntity(coupon.getTitle(),
                coupon.getDescription(),
                coupon.getPrice(),
                coupon.getCompanyId(),
                coupon.getCategoryId(),
                coupon.getStartDate(),
                coupon.getEndDate(),
                coupon.getAmount(),
                coupon.getImageUrl());
        return couponsEntity;
    }

    private void validateCoupon(Coupon coupon) throws ServerException {
        if (coupon.getAmount() < 1) {
            throw new ServerException(ErrorType.INVALID_AMOUNT_OF_COUPONS, coupon.toString());
        }

        if (coupon.getDescription() == null) {
            throw new ServerException(ErrorType.INVALID_COUPON_DESCRIPTION, coupon.toString());
        }

        if (coupon.getDescription().length() > 200) {
            throw new ServerException(ErrorType.INVALID_COUPON_DESCRIPTION, coupon.toString());
        }

        if (coupon.getPrice() <= 0) {
            throw new ServerException(ErrorType.INVALID_PRICE, coupon.toString());
        }


        if (coupon.getTitle() == null) {
            throw new ServerException(ErrorType.INVALID_COUPON_TITLE, coupon.toString());
        }

        if (coupon.getTitle().length() > 45) {
            throw new ServerException(ErrorType.INVALID_COUPON_TITLE, coupon.toString());
        }

        if (coupon.getStartDate() == null || coupon.getEndDate() == null) {
            throw new ServerException(ErrorType.INVALID_END_DATE, coupon.toString());
        }

        if (coupon.getEndDate().before(coupon.getStartDate())) {
            throw new ServerException(ErrorType.INVALID_END_DATE, coupon.toString());
        }

        if (coupon.getImageUrl() != null && coupon.getImageUrl().length() > 100) {
            throw new ServerException(ErrorType.INVALID_IMAGE_URL, coupon.toString());
        }

        if (!this.categoriesLogic.isCategoryIdExists(coupon.getCategoryId())) {
            throw new ServerException(ErrorType.INVALID_CATEGORY_ID, coupon.toString());
        }
    }

    public void deleteExpiredCoupons() throws ServerException{
        LocalDateTime localDateTime = LocalDateTime.now();
        Date currentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        this.couponsDal.deleteExpiredCoupons(currentDate);
    }

    void decreaseCouponAmount(int purchaseAmount, int couponId) throws ServerException {
        Coupon coupon = getInternalCouponById(couponId);
        int newAmount = coupon.getAmount() - purchaseAmount;

        coupon.setAmount(newAmount);
        CouponsEntity couponEntity = convertCouponToCouponsEntity(coupon);

        this.couponsDal.save(couponEntity);
    }

    void increaseCouponAmount(int purchaseAmount, int couponId) throws ServerException {
        Coupon coupon = getInternalCouponById(couponId);
        int newAmount = coupon.getAmount() + purchaseAmount;
        coupon.setAmount(newAmount);
        CouponsEntity couponEntity = convertCouponToCouponsEntity(coupon);
        this.couponsDal.save(couponEntity);
    }
}
