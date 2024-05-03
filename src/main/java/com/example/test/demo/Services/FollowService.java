package com.example.test.demo.Services;

import com.example.test.demo.Http.Request.ToggleFollowRequest;
import com.example.test.demo.Http.Responses.ApiResponse;
import com.example.test.demo.Http.Responses.ErrorResponse;
import com.example.test.demo.Http.Responses.SuccessResponse;
import com.example.test.demo.Models.Comic;
import com.example.test.demo.Models.Follow;
import com.example.test.demo.Models.History;
import com.example.test.demo.Models.User;
import com.example.test.demo.Reponsitories.ComicRepository;
import com.example.test.demo.Reponsitories.FollowRepository;
import com.example.test.demo.Reponsitories.HistoryRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final ComicRepository comicRepository;
    private final MongoTemplate mongoTemplate;

    private static final Integer LIMIT = 36;

    public FollowService(FollowRepository followRepository, ComicRepository comicRepository, MongoTemplate mongoTemplate) {
        this.followRepository = followRepository;
        this.comicRepository = comicRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public ApiResponse<?> toggleHistory(ToggleFollowRequest followRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Comic> comic = this.comicRepository.findComicById(followRequest.getComic_id());
        if (comic.isEmpty()){
            return new ErrorResponse<>("Comic not found!");
        }

        Optional<Follow> findFollow = this.followRepository.findFollowByUsernameAndComicId(user.getUsername(),followRequest.getComic_id());
        if (findFollow.isEmpty()){
            Follow follow = new Follow();
            follow.setComicId(followRequest.getComic_id());
            follow.setUsername(user.getUsername());
            this.followRepository.save(follow);
        }else {
            this.followRepository.delete(findFollow.get());
        }

        return new SuccessResponse<>();
    }

    public ApiResponse<?> list(Optional<Integer> page){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AggregationOperation aggregationOperation = Aggregation.match(Criteria.where("username").is(user.getUsername()));
        Aggregation aggregation = Aggregation.newAggregation(
                aggregationOperation,
                Aggregation.lookup("hr_comics","comic_id","id","comics"),
                Aggregation.lookup("hr_histories","comic_id","comic_id","histories"),
                Aggregation.skip((long) (page.get() - 1) * LIMIT),
                Aggregation.limit(LIMIT)
        );

        Aggregation aggregationCount = Aggregation.newAggregation(aggregationOperation);

        List<Follow> follows = this.mongoTemplate.aggregate(aggregation,"hr_follows",Follow.class).getMappedResults();
        long total = this.mongoTemplate.aggregate(aggregationCount, "hr_follows", Follow.class).getMappedResults().size();

        Map<String, Object> result = new HashMap<>();
        result.put("current_page",page);
        result.put("current_size",follows.size());
        result.put("total",total);
        result.put("total_page",(int) Math.ceil(total / (float) LIMIT));
        result.put("list", follows.stream().map(item -> {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("id", item.getComicId());
            itemMap.put("name", item.getComics().getFirst().getName());
            itemMap.put("another_name", item.getComics().getFirst().getAnotherName());
            itemMap.put("url", item.getComics().getFirst().getUrl());
            itemMap.put("image", item.getComics().getFirst().getImage());
            itemMap.put("categories", item.getComics().getFirst().getCategories());
            itemMap.put("view", item.getComics().getFirst().getView());
            itemMap.put("follow", item.getComics().getFirst().getFollow());
            itemMap.put("description", item.getComics().getFirst().getDescription());
            itemMap.put("chapter", item.getComics().getFirst().getChapter());
            itemMap.put("status", item.getComics().getFirst().getStatus());

            Map<String,Object> chapterMap = new HashMap<>();

            chapterMap.put("id",item.getHistories().getFirst().getChapterId());
            chapterMap.put("name",item.getHistories().getFirst().getChapterName());
            chapterMap.put("url",item.getHistories().getFirst().getChapterUrl());

            itemMap.put("chapter_reading",chapterMap);

            return itemMap;
        }));


        return new SuccessResponse<>(result);
    }
}
