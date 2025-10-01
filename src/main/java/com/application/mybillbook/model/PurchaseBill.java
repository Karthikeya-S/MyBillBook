package com.application.mybillbook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Document(collection = "purchase_bills")
public class PurchaseBill {
    @Id
    private String id;

    private String vendorId;
    private String vendorBusinessName;
    private Date billDate;
    private List<PurchaseBillItem> items;
    private BigDecimal finalSum = BigDecimal.ZERO;
    private BigDecimal paid = BigDecimal.ZERO;

    public PurchaseBill() {
    }

    public String getId() {
        return id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorBusinessName() {
        return vendorBusinessName;
    }

    public void setVendorBusinessName(String vendorBusinessName) {
        this.vendorBusinessName = vendorBusinessName;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public List<PurchaseBillItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseBillItem> items) {
        this.items = items;
    }

    public BigDecimal getFinalSum() {
        return finalSum;
    }

    public void setFinalSum(BigDecimal finalSum) {
        this.finalSum = finalSum;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }
}
