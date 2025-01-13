package com.kira.coupons.dal;

import com.kira.coupons.dto.Company;
import com.kira.coupons.entities.CompaniesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompaniesDal extends JpaRepository<CompaniesEntity, Integer> {
    @Query("SELECT c FROM CompaniesEntity c WHERE c.name = :name")
    CompaniesEntity findCompanyByName(@Param("name") String name);

    @Query("SELECT new com.kira.coupons.dto.Company(c.id,c.name,c.address,c.phone) FROM CompaniesEntity c")
    Page<Company> getListOfCompanies(Pageable pageable);

    @Query("select count(c.id)>0 from CompaniesEntity c where name = :name")
    boolean isCompanyNameNotUnique(@Param("name") String name);
}
