package com.application.mybillbook.Interfaces;


import com.application.mybillbook.Entities.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.UUID;

public interface IAuthorRepository extends MongoRepository<Author, UUID> {
}