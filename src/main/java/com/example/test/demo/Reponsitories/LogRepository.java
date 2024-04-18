package com.example.test.demo.Reponsitories;

import com.example.test.demo.Models.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log,String> {
}
