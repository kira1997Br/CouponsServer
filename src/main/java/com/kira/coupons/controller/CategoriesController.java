package com.kira.coupons.controller;

import com.kira.coupons.dto.Category;
import com.kira.coupons.dto.UserLogin;
import com.kira.coupons.exceptions.ServerException;
import com.kira.coupons.logic.CategoriesLogic;
import com.kira.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController {
    private CategoriesLogic categoriesLogic;

    @Autowired
    public CategoriesController(CategoriesLogic categoriesLogic) {
        this.categoriesLogic = categoriesLogic;
    }

    @PostMapping
    public void createCategory(@RequestBody Category category, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.categoriesLogic.createCategory(category, userLogin.getUserType());
    }

    @PutMapping
    public void updateCategory (@RequestBody Category category, @RequestHeader("Authorization") String token) throws Exception{
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.categoriesLogic.updateCategory(category, userLogin.getUserType());
    }

    @GetMapping("/admin/{id}")
    public Category getCategoryById (@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception{
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.categoriesLogic.getCategoryById(id, userLogin.getUserType());
    }

    @GetMapping
    public Page<Category> getCategories(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size){
        return this.categoriesLogic.getListOfCategories(page, size);
    }



    @DeleteMapping("/{id}")
    public void deleteCategory  (@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception{
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.categoriesLogic.deleteCategory(userLogin.getUserType(), id);
    }

    @GetMapping("/findbyname")
    public Category findCategoryByName(@RequestParam("name") String name) throws ServerException {
        return this.categoriesLogic.findCategoryByName(name);
    }

}
