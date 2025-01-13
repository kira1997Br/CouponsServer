package com.kira.coupons.controller;

import com.kira.coupons.dto.Purchase;
import com.kira.coupons.dto.PurchaseDetails;
import com.kira.coupons.dto.UserLogin;
import com.kira.coupons.logic.PurchasesLogic;
import com.kira.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchasesController {
    private PurchasesLogic purchasesLogic;

    @Autowired
    public PurchasesController(PurchasesLogic purchasesLogic) {
        this.purchasesLogic = purchasesLogic;
    }

    @PostMapping
    public void createPurchase(@RequestBody Purchase purchase, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.purchasesLogic.createPurchase(purchase, userLogin.getUserType());
    }

    @DeleteMapping("/{id}")
    public void deletePurchaseByPurchaseId(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.purchasesLogic.deletePurchase(id, userLogin.getUserType());
    }

    @GetMapping
    public Page<PurchaseDetails> getPurchases(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return purchasesLogic.getPurchasesByAdmin(page, size, userLogin.getUserType());
    }

    @GetMapping("/{id}")
    public PurchaseDetails getPurchaseDetailsById(@PathVariable("id") int purchaseId, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return purchasesLogic.getPurchaseDetailsByPurchaseId(purchaseId, userLogin.getId());
    }


    @GetMapping("/bycustomer")
    public Page<PurchaseDetails> getPurchaseDetailsByCustomer(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return purchasesLogic.getPurchaseDetailsByCustomer(page, size, userLogin.getId());
    }
}
