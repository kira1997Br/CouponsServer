package com.kira.coupons.enums;//package com.kira.coupons.enums;

public enum ErrorType {
    GENERAL_ERROR(400,1000, "A general error has occured please try again later"),
    UNAUTHORIZED(401,1001, "Invalid user name or password"),
    INVALID_COUPON_DESCRIPTION(400,1002, "Invalid coupon description"),
    INVALID_AMOUNT_OF_COUPONS(400,1003, "Invalid amount of coupons"),
    INVALID_COUPON_TITLE(400,1004, "Invalid coupon title"),
    INVALID_PRICE(400,1005, "Invalid coupon price"),
    INVALID_COMPANY_ID(400,1006, "Invalid company id"),
    INVALID_CATEGORY_ID(400,1007, "Invalid category id"),
    INVALID_START_DATE(400,1008, "Invalid start day"),
    INVALID_END_DATE(400,1008, "Invalid end day"),
    INVALID_IMAGE_URL(400,1009, "Invalid image url"),
    INVALID_USER_TYPE(400,1010, "Invalid user type"),
    INVALID_CATEGORY_NAME(400,1011, "Invalid category name"),
    INVALID_NAME(400,1012, "Invalid  name"),
    INVALID_ADDRESS(400,1013, "Invalid  address"),
    INVALID_PHONE_NUMBER(400,1014, "Invalid  number"),
    INVALID_CUSTOMER_ID(400,1015, "Invalid customer id"),
    INVALID_COUPON_ID(400,1016, "Invalid COUPON id"),
    INVALID_AMOUNT_OF_PURCHASES(400,1017, "Invalid amount of purchases"),
    USER_NAME_ALREADY_EXIST(400,1018, "User name already exist"),
    N0T_AUTHORIZED_UPDATE(400,1019, "Not authorized update");


    private int errorNumber;
    private int internalError;
    private String clientErrorMessage;

    ErrorType(int errorNumber, int internalError, String clientErrorMessage) {
        this.errorNumber = errorNumber;
        this.internalError = internalError;
        this.clientErrorMessage = clientErrorMessage;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public int getInternalError() {
        return internalError;
    }

    public String getClientErrorMessage() {
        return clientErrorMessage;
    }
}
