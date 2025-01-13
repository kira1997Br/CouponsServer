package com.kira.coupons.logic;

import com.kira.coupons.dal.ICompaniesDal;
import com.kira.coupons.dto.Company;
import com.kira.coupons.entities.CompaniesEntity;
import com.kira.coupons.enums.ErrorType;
import com.kira.coupons.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompaniesLogic {
    private ICompaniesDal companiesDal;

    @Autowired
    public CompaniesLogic(ICompaniesDal companiesDal) {
        this.companiesDal = companiesDal;
    }

    @Transactional
    public void createCompany (Company company, String userType) throws ServerException, RuntimeException {
        if(!userType.equals("admin")){
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        if (companiesDal.isCompanyNameNotUnique(company.getName())){
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        validateCompany (company);
        CompaniesEntity companiesEntity = convertCompanyToCompanyEntity (company);
        this.companiesDal.save(companiesEntity);
    }


    public void updateCompany (Company company, String userType) throws ServerException {
        if(!userType.equals("admin")){
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        CompaniesEntity companiesEntity = this.companiesDal.findById(company.getId()).get();
        if (this.companiesDal==null){
            throw new ServerException(ErrorType.INVALID_COMPANY_ID);
        }

        validateCompany(company);
        companiesEntity.setName(company.getName());
        companiesEntity.setAddress(company.getAddress());
        companiesEntity.setPhone(company.getPhone());
        this.companiesDal.save(companiesEntity);
    }


    public Company getCompanyById (int id, String userType) throws ServerException {
        if (!userType.equals("admin")){
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        if (!this.companiesDal.existsById(id)){
            throw new ServerException(ErrorType.INVALID_COMPANY_ID);
        }
        CompaniesEntity companiesEntity = this.companiesDal.findById(id).get();
        Company company = convertCompanyEntityToCompany(companiesEntity);
        return company;
    }

    public Company findCompanyByName (String name) throws ServerException {
        CompaniesEntity companiesEntity = this.companiesDal.findCompanyByName(name);
        if (companiesEntity==null){
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }

        Company company = convertCompanyEntityToCompany(companiesEntity);
        return company;
    }

    @Transactional
    public void deleteCompanyById(int id, String userType) throws ServerException, RuntimeException {
        if(!userType.equals("admin")){
            throw new ServerException(ErrorType.UNAUTHORIZED, "No Authorization");
        }
        this.companiesDal.deleteById(id);
    }

    public Page<Company> getListOfCompanies(int page, int size) {
        Pageable pageable = PageRequest.of(page , size);
        return this.companiesDal.getListOfCompanies(pageable);
    }

    private List<Company> convertCompanyEntityToCompanies(List<CompaniesEntity> companyEntities) {
        List<Company> companies = new ArrayList<>();
        for(CompaniesEntity companyEntity: companyEntities){
            Company company = new Company(companyEntity.getId(),
                    companyEntity.getName(),
                    companyEntity.getAddress(),
                    companyEntity.getPhone());
            companies.add(company);
        }
        return companies;

    }

    private Company convertCompanyEntityToCompany(CompaniesEntity companiesEntity) {
        Company company = new Company(companiesEntity.getId(),
                companiesEntity.getName(), companiesEntity.getAddress(),
                companiesEntity.getPhone());
        return company;
    }

    private CompaniesEntity convertCompanyToCompanyEntity(Company company) {
        CompaniesEntity companiesEntity = new CompaniesEntity(company.getId(),
                company.getName(),
                company.getAddress(),
                company.getPhone());
        return companiesEntity;
    }

    private void validateCompany(Company company) throws ServerException {
        if (company.getName() == null || company.getName().length() > 45) {
            throw new ServerException(ErrorType.INVALID_NAME, company.toString());
        }

        if (company.getPhone() != null && (company.getPhone().length() < 9 || company.getPhone().length() > 15)) {
            throw new ServerException(ErrorType.INVALID_PHONE_NUMBER, company.toString());
        }

        if (company.getAddress() != null && company.getAddress().length() > 45) {
            throw new ServerException(ErrorType.INVALID_ADDRESS, company.toString());
        }
    }
}
