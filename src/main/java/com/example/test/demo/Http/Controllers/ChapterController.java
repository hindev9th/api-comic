package com.example.test.demo.Http.Controllers;

import com.example.test.demo.Http.Responses.ApiResponse;
import com.example.test.demo.Services.ChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "chapters")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService){
        this.chapterService = chapterService;
    }

    @GetMapping()
    public ResponseEntity<?> list(@RequestParam String key){
        ApiResponse<?> response = this.chapterService.list(key);
        return response.responseEntity();
    }
}
