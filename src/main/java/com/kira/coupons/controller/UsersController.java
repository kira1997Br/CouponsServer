package com.kira.coupons.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kira.coupons.dto.User;
import com.kira.coupons.dto.UserLogin;
import com.kira.coupons.dto.UserLoginData;
import com.kira.coupons.entities.UsersEntity;
import com.kira.coupons.enums.ErrorType;
import com.kira.coupons.exceptions.ServerException;
import com.kira.coupons.logic.UsersLogic;
import com.kira.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    private UsersLogic usersLogic;
    private UserLoginData userLoginData;

    @Autowired
    public UsersController(UsersLogic usersLogic) {
        this.usersLogic = usersLogic;
    }

    @PostMapping
    public UsersEntity createUserOfTypeCustomer(@RequestBody User user) throws Exception {
        if (!user.getUserType().equals("customer")){
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        return this.usersLogic.createUser(user);
    }

    @PostMapping("/byadmin")
    public UsersEntity createUserOfAnyType(@RequestHeader("Authorization") String token, @RequestBody User user) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        if (!userLogin.getUserType().equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE, "Only an admin can create a user");
        }
        return this.usersLogic.createUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginData userLoginData) throws ServerException, JsonProcessingException {
        return this.usersLogic.login(userLoginData);
    }


    @PutMapping
    public void updateUser(@RequestBody User user, @RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        user.setId(userLogin.getId());
        user.setUserType(userLogin.getUserType());
        user.setCompanyId(userLogin.getCompanyId());
        this.usersLogic.updateUser(user);
    }


    @GetMapping
    public Page<User> getListOfUsers(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.usersLogic.getListOfUsers(userLogin.getUserType(), page, size);
    }


    @DeleteMapping("/deleteMyUser")  //working
    public void deleteMyUser(@RequestHeader("Authorization") String token) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.usersLogic.deleteMyUser(userLogin.getId(), userLogin.getUserType());
    }

    @DeleteMapping("deleteUserByAdmin/{id}")
    public void deleteUserByAdmin(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        this.usersLogic.deleteUserByAdmin(id, userLogin.getUserType());
    }

    @GetMapping("/{id}")
    public User getUserById(@RequestHeader("Authorization") String token, @PathVariable("id") int id) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.usersLogic.getUserById(id, userLogin.getUserType());
    }

    @GetMapping("/bycompanyid")
    public Page<User> getListOfUsersByCompanyId(@RequestHeader("Authorization") String token, @RequestParam("companyId") int companyId, @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) throws Exception {
        UserLogin userLogin = JWTUtils.decodeJWT(token);
        return this.usersLogic.getListOfUsersByCompanyId(companyId, userLogin.getUserType(), page, size);
    }
}
