package com.example.test.demo.Reponsitories;

import com.example.test.demo.Models.Comic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ComicRepository extends MongoRepository<Comic, String> {
}
