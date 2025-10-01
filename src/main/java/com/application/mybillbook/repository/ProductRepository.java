package com.application.mybillbook.repository;

import com.application.mybillbook.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByBusinessIdIn(List<String> businessIds);
}
