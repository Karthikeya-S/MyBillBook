package com.application.mybillbook.repository;

import com.application.mybillbook.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, Integer> {
    Users findByUserName(String userName);
    Users findByEmail(String email);
}