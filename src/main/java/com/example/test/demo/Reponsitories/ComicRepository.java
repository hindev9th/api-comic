package com.example.test.demo.Reponsitories;

import com.example.test.demo.Models.Comic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ComicRepository extends MongoRepository<Comic, String> {
    Optional<Comic> findComicById(String id);
}
