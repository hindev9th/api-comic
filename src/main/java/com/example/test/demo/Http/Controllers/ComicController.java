package com.example.test.demo.Http.Controllers;

import com.example.test.demo.Services.ComicService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "comics")
public class ComicController {
    private final ComicService comicService;

    public ComicController(ComicService comicService){
        this.comicService = comicService;
    }

    @GetMapping(value = "")
    public ResponseEntity<?> list(@RequestParam(defaultValue = "0") int page){
        return this.comicService.list(page).responseEntity();
    }
}
