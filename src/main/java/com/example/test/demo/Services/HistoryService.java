package com.example.test.demo.Services;

import com.example.test.demo.Http.Request.AddHistoryRequest;
import com.example.test.demo.Http.Responses.ApiResponse;
import com.example.test.demo.Http.Responses.ErrorResponse;
import com.example.test.demo.Http.Responses.SuccessResponse;
import com.example.test.demo.Models.Comic;
import com.example.test.demo.Models.History;
import com.example.test.demo.Models.User;
import com.example.test.demo.Reponsitories.ComicRepository;
import com.example.test.demo.Reponsitories.HistoryRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final ComicRepository comicRepository;
    private final MongoTemplate mongoTemplate;

    private static final Integer LIMIT = 36;

    public HistoryService(HistoryRepository historyRepository, ComicRepository comicRepository, MongoTemplate mongoTemplate) {
        this.historyRepository = historyRepository;
        this.comicRepository = comicRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public ApiResponse<?> asyncHistory(AddHistoryRequest historyRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Comic> comic = this.comicRepository.findComicById(historyRequest.getComic_id());
        if (comic.isEmpty()){
            return new ErrorResponse<>("Comic not found!");
        }

        Optional<History> findHistory = this.historyRepository.findHistoriesByUsernameAndComicId(user.getUsername(),comic.get().getId());
        if (findHistory.isEmpty()){
            History history = new History();
            history.setComicId(comic.get().getId());
            history.setUsername(user.getUsername());
            history.setChapterId(historyRequest.getChapter_id());
            history.setChapterName(historyRequest.getChapter_name());
            history.setChapterUrl(historyRequest.getChapter_url());
            this.historyRepository.save(history);
        }else {
            Query query = new Query();
            query.addCriteria(Criteria.where("username").is(user.getUsername()));
            query.addCriteria(Criteria.where("comicId").is(comic.get().getId()));

            Update update = new Update();
            update.set("chapter_id",historyRequest.getChapter_id());
            update.set("chapter_name",historyRequest.getChapter_name());
            update.set("chapter_url",historyRequest.getChapter_url());

            this.mongoTemplate.findAndModify(query,update,History.class);
        }

        return new SuccessResponse<>();
    }

    public ApiResponse<?> list(Optional<Integer> page){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AggregationOperation aggregationOperation = Aggregation.match(Criteria.where("username").is(user.getUsername()));
        Aggregation aggregation = Aggregation.newAggregation(
                aggregationOperation,
                Aggregation.lookup("hr_comics","comic_id","id","comics"),
                Aggregation.skip((long) (page.get() - 1) * LIMIT),
                Aggregation.limit(LIMIT)
        );

        Aggregation aggregationCount = Aggregation.newAggregation(aggregationOperation);

        List<History> histories = this.mongoTemplate.aggregate(aggregation,"hr_histories",History.class).getMappedResults();
        long total = this.mongoTemplate.aggregate(aggregationCount, "hr_histories", History.class).getMappedResults().size();

        Map<String, Object> result = new HashMap<>();
        result.put("current_page",page);
        result.put("current_size",histories.size());
        result.put("total",total);
        result.put("total_page",(int) Math.ceil(total / (float) LIMIT));
        result.put("list", histories.stream().map(item -> {
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

            chapterMap.put("id",item.getChapterId());
            chapterMap.put("name",item.getChapterName());
            chapterMap.put("url",item.getChapterUrl());

            itemMap.put("chapter_reading",chapterMap);

            return itemMap;
        }));


        return new SuccessResponse<>(result);
    }

}
