package com.example.test.demo.Services;

import com.example.test.demo.Http.Responses.ApiResponse;
import com.example.test.demo.Http.Responses.ErrorResponse;
import com.example.test.demo.Http.Responses.SuccessResponse;
import com.example.test.demo.Models.Config;
import com.example.test.demo.Reponsitories.ConfigRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ConfigService {
    private final ConfigRepository configRepository;

    public ConfigService(ConfigRepository configRepository){
        this.configRepository = configRepository;
    }

    public ApiResponse<?> getCrawlUrl(){
        Optional<Config> config = this.configRepository.findByType(Config.TYPE.BASE_CRAWL_URL.name());
        if (config.isPresent()){
            return new SuccessResponse<>(config.get().getContent());
        }
        return new ErrorResponse<>();
    }
}
