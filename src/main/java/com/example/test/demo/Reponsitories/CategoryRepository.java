package com.example.test.demo.Reponsitories;

import com.example.test.demo.Models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
