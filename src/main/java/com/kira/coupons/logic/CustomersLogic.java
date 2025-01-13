package com.kira.coupons.logic;

import com.kira.coupons.dal.ICustomersDal;
import com.kira.coupons.dto.Customer;
import com.kira.coupons.dto.CustomerDetails;
import com.kira.coupons.dto.UserLogin;
import com.kira.coupons.entities.CustomersEntity;
import com.kira.coupons.entities.UsersEntity;
import com.kira.coupons.enums.ErrorType;
import com.kira.coupons.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomersLogic {
    private ICustomersDal customersDal;
    private UsersLogic usersLogic;


    @Autowired
    public CustomersLogic(ICustomersDal customersDal, UsersLogic usersLogic) {
        this.customersDal = customersDal;
        this.usersLogic = usersLogic;
    }

    @Transactional
    public void createCustomer(Customer customer) throws ServerException, RuntimeException {
        validateCustomer(customer);
        customer.getUser().setUserType("customer");
        customer.getUser().setCompanyId(null);
        UsersEntity usersEntity = usersLogic.createUser(customer.getUser());

        CustomersEntity customersEntity = convertCustomerToCustomerEntity(customer);
        customersEntity.setId(usersEntity.getId());
        this.customersDal.save(customersEntity);
    }

    public void updateCustomer(Customer customer) throws ServerException {
        validateCustomer(customer);
        CustomersEntity customersEntity = convertCustomerToCustomerEntity(customer);
        this.customersDal.save(customersEntity);
    }

    @Transactional
    public void deleteMyCustomer(UserLogin userLogin) throws ServerException, RuntimeException {
        this.customersDal.deleteById(userLogin.getId());
    }

    public CustomerDetails getCustomerDetailsById(int id) throws ServerException {
        if(!this.customersDal.existsById(id)){
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        return this.customersDal.getCustomerDetailsById(id);
    }

    public CustomerDetails getCustomerByAdmin(int id, String userType) throws ServerException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.INVALID_USER_TYPE, "you new to do admin from this ");
        }
        return this.customersDal.getCustomerDetailsById(id);
    }

    public Page<CustomerDetails> getListOfCustomersDetails (String userType, int page, int size) throws ServerException {
        if (!userType.equals("admin")){
            throw new ServerException(ErrorType.INVALID_USER_TYPE);
        }
        Pageable pageable = PageRequest.of(page , size);
        return this.customersDal.getCustomersDetails(pageable);
    }

    @Transactional
    public void deleteCustomerByAdmin(String userType, int customerId) throws ServerException, RuntimeException {
        if (!userType.equals("admin")) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
        this.customersDal.deleteById(customerId);
    }

    private CustomersEntity convertCustomerToCustomerEntity(Customer customer) {
        CustomersEntity customersEntity = new CustomersEntity(customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone());
        return customersEntity;
    }


    private void validateCustomer(Customer customer) throws ServerException {
        if (customer.getName() == null || customer.getName().length() > 45) {
            throw new ServerException(ErrorType.INVALID_NAME, customer.toString());
        }

        if (customer.getPhone() == null) {
            throw new ServerException(ErrorType.INVALID_PHONE_NUMBER, customer.toString());
        }

        if (customer.getPhone() != null && (customer.getPhone().length() < 9 || customer.getPhone().length() > 15)) {
            throw new ServerException(ErrorType.INVALID_PHONE_NUMBER, customer.toString());
        }
        if (customer.getAddress() == null) {
            throw new ServerException(ErrorType.INVALID_ADDRESS, customer.toString());
        }

        if (customer.getAddress() != null && customer.getAddress().length() > 45) {
            throw new ServerException(ErrorType.INVALID_ADDRESS, customer.toString());
        }

    }
}

