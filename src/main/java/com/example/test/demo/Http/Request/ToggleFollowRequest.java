package com.example.test.demo.Http.Request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ToggleFollowRequest {
    @NotEmpty
    private String comic_id;
}
