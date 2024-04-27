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
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;

@Service
public class ComicService {
    private final ComicRepository comicRepository;
    private final static int LIMIT = 16;

    private ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public ApiResponse<?> list(int page) {
        Pageable pageable = PageRequest.of(page, LIMIT, Sort.by("updated_at").descending());
        Page<Comic> comics = this.comicRepository.findAll(pageable);

        Map<String, Object> result = new HashMap<>();

        result.put("list", comics.getContent().stream().map(item -> {
            Map<String, Object> itemOj = new HashMap<>();
            itemOj.put("id", item.getId());
            itemOj.put("name", item.getName());
            itemOj.put("another_name", item.getAnotherName());
            itemOj.put("url", item.getUrl());
            itemOj.put("image", "https:" + CommonHelper.getEnv("BASE_IMAGE_URL") + item.getImage());
            itemOj.put("categories", item.getCategories());
            itemOj.put("view", item.getView());
            itemOj.put("follow", item.getFollow());
            itemOj.put("description", item.getDescription());
            itemOj.put("chapter", item.getChapter());
            itemOj.put("status", item.getStatus());
            itemOj.put("api_chapter", CommonHelper.getEnv("BASE_COMIC_URL") + "Comic/Services/ComicService.asmx/ProcessChapterList?comicId=" + item.getId());

            return itemOj;
        }));

        result.put("total_page", comics.getTotalPages());
        result.put("size_page", LIMIT);
        result.put("current_page", page);
        result.put("total_item", comics.getTotalElements());
        result.put("current_size", comics.getContent().size());

        return new SuccessResponse<>(result);
    }
}
