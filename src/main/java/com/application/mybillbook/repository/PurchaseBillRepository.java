package com.application.mybillbook.repository;

import com.application.mybillbook.model.PurchaseBill;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseBillRepository extends MongoRepository<PurchaseBill, String> {
    List<PurchaseBill> findByVendorId(String vendorId);
}
