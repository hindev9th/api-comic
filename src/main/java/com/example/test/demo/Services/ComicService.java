package com.example.test.demo.Services;

import com.example.test.demo.Helpers.CommonHelper;
import com.example.test.demo.Http.Responses.ApiResponse;
import com.example.test.demo.Http.Responses.SuccessResponse;
import com.example.test.demo.Models.Comic;
import com.example.test.demo.Network.ParseHtml;
import com.example.test.demo.Reponsitories.ComicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ComicService {
    private final ComicRepository comicRepository;
    private final MongoTemplate mongoTemplate;
    private final static int LIMIT = 16;

    private ComicService(ComicRepository comicRepository, MongoTemplate mongoTemplate) {
        this.comicRepository = comicRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public ApiResponse<?> list(int page, Optional<String> category, Optional<String> searchKey, Optional<String> sortBy, Optional<String> sortDirection) {
        Query query = new Query();
        Sort sort;
        if (sortDirection.get().equalsIgnoreCase("DESC")) {
            sort = Sort.by(sortBy.get()).descending();
        } else {
            sort = Sort.by(sortBy.get()).ascending();
        }

        Criteria criteria = new Criteria().orOperator(
                Criteria.where("name").regex(searchKey.get(), "i"),
                Criteria.where("another_name").regex(searchKey.get(), "i"),
                Criteria.where("description").regex(searchKey.get(), "i")
        );

        Criteria criteria1 = Criteria.where("categories").regex(category.get(), "i");
        Pageable pageable = PageRequest.of(page -1, LIMIT, sort);

        query.addCriteria(criteria);
        query.addCriteria(criteria1);
        long total = this.mongoTemplate.count(query,Comic.class);
        query.with(pageable);
        List<Comic> comics = this.mongoTemplate.find(query,Comic.class);
        Map<String, Object> result = new HashMap<>();

        result.put("list", comics.stream().map(item -> {
            Map<String, Object> itemOj = new HashMap<>();
            itemOj.put("id", item.getId());
            itemOj.put("name", item.getName());
            itemOj.put("another_name", item.getAnotherName());
            itemOj.put("url", item.getUrl());
            itemOj.put("image", item.getImage());
            itemOj.put("categories", item.getCategories());
            itemOj.put("view", item.getView());
            itemOj.put("follow", item.getFollow());
            itemOj.put("description", item.getDescription());
            itemOj.put("chapter", item.getChapter());
            itemOj.put("status", item.getStatus());
            itemOj.put("api_chapter", CommonHelper.getEnv("BASE_COMIC_URL") + "Comic/Services/ComicService.asmx/ProcessChapterList?comicId=" + item.getId());

            return itemOj;
        }));

        result.put("total_page", total / LIMIT);
        result.put("size_page", LIMIT);
        result.put("current_page", page);
        result.put("total_item", total);
        result.put("current_size", comics.size());

        return new SuccessResponse<>(result);
    }
}
