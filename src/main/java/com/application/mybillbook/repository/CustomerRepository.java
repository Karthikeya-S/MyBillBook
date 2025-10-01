package com.application.mybillbook.repository;

import com.application.mybillbook.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByBusinessId(String businessId);
}
