package com.example.test.demo.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "hr_chapters")
public class Chapter extends BaseMongoModel {
    @Field(name = "id")
    private String id;
    @Field(name = "name")
    private String name;
    @Field(name = "url")
    private String url;
    @Field(name = "time")
    private String time;
}
