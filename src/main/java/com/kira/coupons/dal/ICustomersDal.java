package com.kira.coupons.dal;

import com.kira.coupons.dto.CustomerDetails;
import com.kira.coupons.entities.CustomersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICustomersDal extends JpaRepository<CustomersEntity, Integer> {
    @Query("SELECT new com.kira.coupons.dto.CustomerDetails(c.name , c.user.username ,c.address ,c.phone ) " +
            "FROM CustomersEntity c " +
            "WHERE c.user.id = :id")
    CustomerDetails getCustomerDetailsById(@Param("id") int id);

    @Query("SELECT NEW com.kira.coupons.dto.CustomerDetails(c.id, c.name, c.user.username, c.address, " +
            "c.phone) FROM CustomersEntity c")
    Page<CustomerDetails> getCustomersDetails(Pageable pageable);
}
