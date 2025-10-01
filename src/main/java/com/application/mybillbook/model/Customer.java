package com.application.mybillbook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    private String customerId;       // A custom ID you can assign (optional unique)
    private String customerName;
    private String gstinNo;
    private String mobile;
    private String email;

    private String doorNo;
    private String street;
    private String area;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    private BigDecimal totalBalance = BigDecimal.ZERO;

    private String businessId; // To link this customer to the business they belong to

    public Customer() {
    }

    public Customer(String customerId, String customerName, String gstinNo, String mobile,
                    String email, String doorNo, String street, String area, String city,
                    String state, String country, String pinCode, String businessId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.gstinNo = gstinNo;
        this.mobile = mobile;
        this.email = email;
        this.doorNo = doorNo;
        this.street = street;
        this.area = area;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
        this.businessId = businessId;
        this.totalBalance = BigDecimal.ZERO;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGstinNo() {
        return gstinNo;
    }

    public void setGstinNo(String gstinNo) {
        this.gstinNo = gstinNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
