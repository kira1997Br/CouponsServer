package com.kira.coupons.dal;

import com.kira.coupons.dto.User;
import com.kira.coupons.entities.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUsersDal extends JpaRepository<UsersEntity, Integer> {

    @Query("select u from UsersEntity u where u.username = :username and u.password = :password")
    UsersEntity login(@Param("username") String username, @Param("password") String password);


    @Query ("select count (u.id) > 0 from UsersEntity u where u.username = :username")
    Boolean isUserNameNotUnique (@Param("username") String username);

    @Query("SELECT u FROM UsersEntity u WHERE u.username = :username")
    UsersEntity findByUserName(@Param("username") String username);

    @Query("select new com.kira.coupons.dto.User(u.id,u.username,u.userType,u.company.id) " +
            "from UsersEntity u WHERE u.company.id = :companyId")
    Page<User> findUsersByCompanyId(@Param("companyId") int companyId, Pageable pageable);


    @Query("select new com.kira.coupons.dto.User(u.id,u.username,u.userType,u.company.id) from UsersEntity u")
    Page<User> getAllUsers(Pageable pageable);
}
