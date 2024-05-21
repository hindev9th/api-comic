package com.example.test.demo.Http.Controllers;

import com.example.test.demo.Services.ConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "configs")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService){
        this.configService = configService;
    }

    @GetMapping(value = "/crawl-url")
    public ResponseEntity<?> getCrawlUrl(){
        return this.configService.getCrawlUrl().responseEntity();
    }
}
