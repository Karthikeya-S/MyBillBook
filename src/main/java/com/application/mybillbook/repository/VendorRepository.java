package com.application.mybillbook.repository;

import com.application.mybillbook.model.Vendors;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VendorRepository extends MongoRepository<Vendors, String> {
    List<Vendors> findByBusinessIdIn(List<String> businessIds);
}
