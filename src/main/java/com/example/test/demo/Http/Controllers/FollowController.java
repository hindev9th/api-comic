package com.example.test.demo.Http.Controllers;

import com.example.test.demo.Http.Request.ToggleFollowRequest;
import com.example.test.demo.Services.FollowService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "follows")
public class FollowController {

    private final FollowService followService;
    public FollowController(FollowService followService){
        this.followService = followService;
    }

    @PostMapping(value = "",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> toggle(@Valid @RequestBody ToggleFollowRequest followRequest){
        return this.followService.toggleHistory(followRequest).responseEntity();
    }

    @GetMapping(value = "")
    public ResponseEntity<?> list(@RequestParam(required = false, defaultValue = "1") Optional<Integer> page){
        return this.followService.list(page).responseEntity();
    }
}
