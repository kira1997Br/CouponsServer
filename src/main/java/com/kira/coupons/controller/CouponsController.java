package com.kira.coupons.controller;

import com.kira.coupons.dto.Coupon;
import com.kira.coupons.dto.CouponDetails;
import com.kira.coupons.dto.User;
import com.kira.coupons.dto.UserLogin;
import com.kira.coupons.exceptions.ServerException;
import com.kira.coupons.logic.CouponsLogic;
import com.kira.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
public class CouponsController {
    private CouponsLogic couponsLogic;
    private UserLogin userLogin;

    @Autowired
    public CouponsController(CouponsLogic couponsLogic) {
        this.couponsLogic = couponsLogic;
    }

    @PostMapping("/bycompany")
    public void createCouponByCompany(@RequestBody Coupon coupon, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        coupon.setCompanyId(userLogin.getCompanyId());
        this.couponsLogic.createCouponByCompany(coupon, userLogin);
    }

    @PostMapping("/byadmin")
    public void createCouponByAdmin(@RequestBody Coupon coupon, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.couponsLogic.createCouponByAdmin(coupon, userLogin.getUserType());
    }


    @PutMapping
    public void updateCouponByCompany(@RequestBody Coupon coupon, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        coupon.setCompanyId(userLogin.getCompanyId());
        this.couponsLogic.updateCouponByCompany(coupon, userLogin);
    }

    @PutMapping("/byadmin")
    public void updateCouponByAdmin(@RequestBody Coupon coupon, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.couponsLogic.updateCouponByAdmin(coupon, userLogin.getUserType());
    }

    @GetMapping
    public Page<CouponDetails> getListOfCoupons(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return this.couponsLogic.getListOfCoupons(page, size);
    }

//    @GetMapping("/{id}")
//    public Coupon getCouponById(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
//        UserLogin userLogin = JWTUtils.decodeJWT(token);
//        return this.couponsLogic.getCouponById(id, this.userLogin.getUserType());
//    }

    @GetMapping ("/{id}")
    public CouponDetails getCouponDetailsById(@PathVariable("id") int id) throws ServerException {
        return this.couponsLogic.getCouponDetailsById(id);
    }



    @GetMapping("/bycompanyname")
    public Page<CouponDetails> findCouponsByCompanyName(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size, @RequestParam("companyName") String companyName) throws ServerException {
        return this.couponsLogic.getCouponsByCompanyName(page, size, companyName);
    }

    @GetMapping("/bycategoryname")
    public Page<CouponDetails> findCouponsByCategoryName(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size, @RequestParam("categoryName") String categoryName) throws ServerException {
        return this.couponsLogic.getCouponsByCategoryName(page, size, categoryName);
    }


    @DeleteMapping("/Admin/{id}")
    public void deleteCouponByAdmin(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.couponsLogic.deleteCouponByAdmin(id, userLogin.getUserType());
    }


    @DeleteMapping("/{id}")
    public void deleteCouponByUserCompany(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.couponsLogic.deleteCouponByCompany(id, userLogin);
    }


    @GetMapping("/bytitle")
    public Page<CouponDetails> getListOfCouponsByTitle(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size, @RequestParam("title") String title) throws ServerException {
        return this.couponsLogic.getListOfCouponsByTitle(page, size, title);
    }

    @GetMapping("/bycompanyid")
    public Page<Coupon> getListOfCouponsByCompanyId(@RequestHeader("Authorization") String token,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.couponsLogic.getCouponsByCompanyId(page, size, userLogin.getCompanyId());
    }





}

