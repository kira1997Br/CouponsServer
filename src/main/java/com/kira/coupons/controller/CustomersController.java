package com.kira.coupons.controller;

import com.kira.coupons.dto.Customer;
import com.kira.coupons.dto.CustomerDetails;
import com.kira.coupons.dto.UserLogin;
import com.kira.coupons.dto.UserLoginData;
import com.kira.coupons.logic.CustomersLogic;
import com.kira.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomersController {
    private CustomersLogic customersLogic;
    private UserLoginData userLoginData;

    @Autowired
    public CustomersController(CustomersLogic customersLogic) {
        this.customersLogic = customersLogic;
    }

    @PostMapping
    public void createCustomer(@RequestBody Customer customer) throws Exception {
        customer.getUser().setUserType("customer");
        this.customersLogic.createCustomer(customer);
    }

    @PutMapping
    public void updateCustomer(@RequestBody Customer customer, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        customer.setId(userLogin.getId());
        this.customersLogic.updateCustomer(customer);
    }

    @DeleteMapping
    public void deleteMyCustomer(@RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.customersLogic.deleteMyCustomer(userLogin);
    }

    @GetMapping("/mycustomer")
    public CustomerDetails getCustomerDetailsById(@RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.customersLogic.getCustomerDetailsById(userLogin.getId());
    }


    @GetMapping("/byadmin/{id}")
    public CustomerDetails getCustomerByAdmin(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.customersLogic.getCustomerByAdmin(id, userLogin.getUserType());
    }

    @GetMapping
    public Page<CustomerDetails> getListOfCustomerDetails(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.customersLogic.getListOfCustomersDetails(userLogin.getUserType(), page, size);
    }

    @DeleteMapping("/byAdmin/{id}")
    public void deleteCustomerByAdmin(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.customersLogic.deleteCustomerByAdmin(userLogin.getUserType(), id);
    }
}
