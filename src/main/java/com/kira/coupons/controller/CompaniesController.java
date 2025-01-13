package com.kira.coupons.controller;

import com.kira.coupons.dto.Company;
import com.kira.coupons.dto.UserLogin;
import com.kira.coupons.exceptions.ServerException;
import com.kira.coupons.logic.CompaniesLogic;
import com.kira.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompaniesController {
    private CompaniesLogic companiesLogic;

    @Autowired
    public CompaniesController(CompaniesLogic companiesLogic) {
        this.companiesLogic = companiesLogic;
    }


    @PostMapping
    public void createCompany(@RequestBody Company company, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.companiesLogic.createCompany(company, userLogin.getUserType());
    }

    @PutMapping
    public void updateCompany(@RequestBody Company company, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin  = JWTUtils.decodeJWT(token);
        this.companiesLogic.updateCompany(company, userLogin.getUserType());
    }

    @GetMapping("/{id}")
    public Company getCompanyById (@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception{
        UserLogin userLogin  = JWTUtils.decodeJWT(token);
        return this.companiesLogic.getCompanyById(id,userLogin.getUserType());
    }

    @GetMapping ("/findbyname")
    public Company findCompanyByName (@RequestParam ("name") String name) throws ServerException {
        return this.companiesLogic.findCompanyByName(name);
    }

    @DeleteMapping("/{id}")
    public void deleteCompanyById(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin  = JWTUtils.decodeJWT(token);
        this.companiesLogic.deleteCompanyById(id, userLogin.getUserType());
    }

    @GetMapping
    public Page<Company> getCompanies(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return this.companiesLogic.getListOfCompanies(page, size);

    }
}