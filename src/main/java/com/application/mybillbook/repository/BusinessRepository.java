package com.application.mybillbook.repository;

import com.application.mybillbook.model.Business;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BusinessRepository extends MongoRepository<Business, String> {
    List<Business> findByOwnerId(String ownerId);

    List<Business> findByOwnerName(List<String> userName);
}
