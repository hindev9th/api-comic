package com.example.test.demo.Reponsitories;

import com.example.test.demo.Models.Config;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConfigRepository extends MongoRepository<Config, String > {
    public Optional<Config> findByType(String type);
}
