package com.application.mybillbook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "businesses")
public class Business {
    @Id
    private String id;

    private String businessName;

    // Address
    private String doorNo;
    private String street;
    private String area;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    private String mobile;
    private String email;

    private String gstinNo;
    private boolean enableGst;

    private String ownerName; // full name of the user
    private String ownerId;   // login id (username) of the user

    private double debtLiabilities = 0.0;

    public Business() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    public String getGstinNo() {
        return gstinNo;
    }

    public void setGstinNo(String gstinNo) {
        this.gstinNo = gstinNo;
    }

    public boolean isEnableGst() {
        return enableGst;
    }

    public void setEnableGst(boolean enableGst) {
        this.enableGst = enableGst;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public double getDebtLiabilities() {
        return debtLiabilities;
    }

    public void setDebtLiabilities(double debtLiabilities) {
        this.debtLiabilities = debtLiabilities;
    }
}
