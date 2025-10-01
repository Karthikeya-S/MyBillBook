package com.application.mybillbook.repository;


import com.application.mybillbook.config.CounterConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CounterConfigRepository extends MongoRepository<CounterConfig, String> {
}
