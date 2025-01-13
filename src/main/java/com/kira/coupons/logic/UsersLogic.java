package com.kira.coupons.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kira.coupons.dal.IUsersDal;
import com.kira.coupons.dto.User;
import com.kira.coupons.dto.UserLogin;
import com.kira.coupons.dto.UserLoginData;
import com.kira.coupons.entities.UsersEntity;
import com.kira.coupons.enums.ErrorType;
import com.kira.coupons.exceptions.ServerException;
import com.kira.coupons.utils.JWTUtils;
import com.kira.coupons.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsersLogic {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,}|co\\.il)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private IUsersDal usersDal;
    private UserLoginData userLoginData;
    private UserLogin userLogin;


    @Autowired
    public UsersLogic(IUsersDal usersDal) {
        this.usersDal = usersDal;
    }

    public String login(UserLoginData userLoginData) throws ServerException, JsonProcessingException {
        UsersEntity usersEntity = this.usersDal.login(userLoginData.getUsername(), userLoginData.getPassword());
        if (usersEntity == null) {
            throw new ServerException(ErrorType.UNAUTHORIZED);
        }

        StatisticsUtils.sendStatistics(userLoginData.getUsername(), "login");

        Integer companyId;
        if (usersEntity.getCompany() == null) {
            companyId = null;
        } else {
            companyId = usersEntity.getCompany().getId();
        }

        UserLogin userLoginDetails = new UserLogin(usersEntity.getId(), usersEntity.getUserType(), companyId);
        String token = JWTUtils.createJWT(userLoginDetails);
        return token;
    }



    @Transactional
    public UsersEntity createUser(User user) throws ServerException, RuntimeException {
        validateUser(user);
        if (!isUserNameNotUnique(user)) {
            throw new ServerException(ErrorType.USER_NAME_ALREADY_EXIST);
        }

        UsersEntity usersEntity = convertUserToUserEntity(user);
        return this.usersDal.save(usersEntity);
    }

//    @Transactional
//    public UsersEntity createUserOfAnyType(User user, String userType) throws ServerException, RuntimeException {
//        if (!userType.equals("admin")){
//            throw new ServerException(ErrorType.INVALID_USER_TYPE);
//        }
//        validateUser(user);
//        if (!isUserNameNotUnique(user)) {
//            throw new ServerException(ErrorType.USER_NAME_ALREADY_EXIST);
//        }
//        UsersEntity usersEntity = convertUserToUserEntity(user);
//        return this.usersDal.save(usersEntity);
//    }

    public void updateUser(User user) throws ServerException {
        validateUser(user);
        UsersEntity userNameBeforeUpdate = this.usersDal.findById(user.getId()).get();

        if (userNameBeforeUpdate == null) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }

        //If the username is different from what it was before, it still must remain unique.
        if (!userNameBeforeUpdate.getUsername().equals(user.getUsername())) {
            if (this.usersDal.isUserNameNotUnique(user.getUsername())) {
                throw new ServerException(ErrorType.GENERAL_ERROR, user.toString());
            }
        }

        UsersEntity userEntity = convertUserToUserEntity(user);
        this.usersDal.save(userEntity);
    }


    public void deleteMyUser(int id, String userType) throws ServerException {
        if (userType.equals("company")) {
            throw new ServerException(ErrorType.GENERAL_ERROR, "invalid user type");
        }
        this.usersDal.deleteById(id);
    }

    public void deleteUserByAdmin(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.GENERAL_ERROR, "No Authorization");
        }
        this.usersDal.deleteById(id);
    }



    public Page<User> getListOfUsers(String userType, int page, int size) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        Pageable pageable = PageRequest.of(page , size);
        return this.usersDal.getAllUsers(pageable);
    }

    public User getUserById(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        if (!this.usersDal.existsById(id)) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        UsersEntity usersEntity = this.usersDal.findById(id).get();
        User user = convertUserEntityToUser(usersEntity);
        return user;
    }

    public Page<User> getListOfUsersByCompanyId (int companyId, String userType, int page, int size)throws ServerException{
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        Pageable pageable = PageRequest.of(page , size);
        return usersDal.findUsersByCompanyId( companyId, pageable);
    }


    private User convertUserEntityToUser(UsersEntity userEntity) {

        Integer companyId = null;
        if (userEntity.getCompany() != null) {
            companyId = userEntity.getCompany().getId();
        }
        User user = new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getUserType(),
                companyId);
        return user;
    }


    private List<User> convertUserEntityToUserList(List<UsersEntity> usersEntities) {
        List<User> users = new ArrayList<>();
        for (UsersEntity userEntity : usersEntities) {
            Integer companyId = null;  // Initialize companyId as null

            // Check if userEntity has a company before getting the ID
            if (userEntity.getCompany() != null) {
                companyId = userEntity.getCompany().getId();  // Assign companyId if company exists
            }

            User user = new User(
                    userEntity.getId(),
                    userEntity.getUsername(),
                    userEntity.getUserType(),
                    companyId);

            users.add(user);
        }

        return users;
    }


    private UsersEntity convertUserToUserEntity(User user) {
        UsersEntity usersEntity = new UsersEntity(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getUserType(),
                user.getCompanyId());
        return usersEntity;
    }

    private boolean isUserNameNotUnique(User user) {
        UsersEntity userToCheck = this.usersDal.findByUserName(user.getUsername());
        if (userToCheck == null) {
            return true;
        }
        return false;
    }

    private void validateUser(User user) throws ServerException {
        if (user.getPassword() == null || user.getPassword().length() > 45) {
            throw new ServerException(ErrorType.UNAUTHORIZED, user.toString());
        }

        if (user.getUsername() == null || user.getUsername().length() > 45) {
            throw new ServerException(ErrorType.UNAUTHORIZED, user.toString());
        }


        if (!(user.getUserType().equals("customer") ||
                user.getUserType().equals("company") ||
                user.getUserType().equals("admin"))) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE, "Invalid user type: " + user.getUserType());
        }
        if (user.getUserType() == null) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE, "Invalid user type: " + user.getUserType());
        }


        if (user.getUserType().equals("company") && (user.getCompanyId() == null || user.getCompanyId() < 1)) {
            throw new ServerException(ErrorType.INVALID_COMPANY_ID, user.toString());
        }

        if (!isValidateEmail(user.getUsername())) {
            throw new ServerException(ErrorType.UNAUTHORIZED, user.toString());
        }

    }

    private boolean isValidateEmail(String username) {
        Matcher matcher = UsersLogic.EMAIL_PATTERN.matcher(username);
        return matcher.matches();
    }
}

