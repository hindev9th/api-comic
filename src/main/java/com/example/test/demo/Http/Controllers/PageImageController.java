package com.example.test.demo.Http.Controllers;

import com.example.test.demo.Services.PageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "pages")
public class PageImageController {

    private final PageService pageService;

    public PageImageController(PageService pageService){
        this.pageService = pageService;
    }

    @GetMapping(value = "")
    public ResponseEntity<?> getList(@RequestParam Optional<String> url){
        return this.pageService.getList(url).responseEntity();
    }

}
