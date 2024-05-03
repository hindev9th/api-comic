package com.example.test.demo.Http.Request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class AddHistoryRequest {
    @NotEmpty
    private String comic_id;
    @NotEmpty
    private String chapter_id;
    @NotEmpty
    private String chapter_name;
    @NotEmpty
    private String chapter_url;
}
