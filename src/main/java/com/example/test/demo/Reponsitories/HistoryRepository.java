package com.example.test.demo.Reponsitories;

import com.example.test.demo.Models.History;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HistoryRepository extends MongoRepository<History, String> {
    Optional<History> findHistoriesByUsernameAndComicId(String username, String comicId);
}
