package com.example.test.demo.Reponsitories;

import com.example.test.demo.Models.Follow;
import com.example.test.demo.Models.History;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FollowRepository extends MongoRepository<Follow, String> {
    Optional<Follow> findFollowByUsernameAndComicId(String username, String comicId);
}
