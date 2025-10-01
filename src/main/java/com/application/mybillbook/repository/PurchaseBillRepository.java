package com.application.mybillbook.repository;

import com.application.mybillbook.model.PurchaseBill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseBillRepository extends MongoRepository<PurchaseBill, String> {
}
