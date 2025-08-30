package com.application.mybillbook.dao;

import com.application.mybillbook.Entities.Author;
import org.apache.catalina.Store;
import org.instancio.Instancio;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Optional;

public class CreateAuthor {
    Author author = Instancio.create(Author.class);
    private MongoOperations authorRepository;

    Author savedAuthor = authorRepository.save(author);

    String updatedName = "sundarrk";
    savedAuthor.setName(updatedName);
    authorRepository.save(savedAuthor);

    Optional<Author> updatedAuthor = authorRepository.findById(savedAuthor.getId());
    assertThat(updatedAuthor)
  .isPresent()
  .get()
  .extracting(Author::getName)
  .isEqualTo(updatedName);
}
