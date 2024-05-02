package com.example.test.demo.Http.Controllers;

import com.example.test.demo.Services.ComicService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "comics")
public class ComicController {
    private final ComicService comicService;

    public ComicController(ComicService comicService) {
        this.comicService = comicService;
    }

    @GetMapping(value = "")
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") Optional<String> p,
            @RequestParam(defaultValue = "updated_at") Optional<String> sort_by,
            @RequestParam(defaultValue = "DESC") Optional<String> sort_direction,
            @RequestParam(defaultValue = "") Optional<String> category) {
        return this.comicService.list(page, category, p, sort_by, sort_direction).responseEntity();
    }
}
