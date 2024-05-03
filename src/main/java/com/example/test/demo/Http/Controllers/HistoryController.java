package com.example.test.demo.Http.Controllers;

import com.example.test.demo.Http.Request.AddHistoryRequest;
import com.example.test.demo.Services.HistoryService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "histories")
public class HistoryController {
    private final HistoryService historyService;
    public HistoryController(HistoryService historyService){
        this.historyService = historyService;
    }

    @GetMapping(value = "")
    public ResponseEntity<?> list(@RequestParam(required = false, defaultValue = "1") Optional<Integer> page){
        return this.historyService.list(page).responseEntity();
    }

    @PostMapping(value = "",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> async(@Valid @RequestBody AddHistoryRequest historyRequest){
        return this.historyService.asyncHistory(historyRequest).responseEntity();
    }
}
