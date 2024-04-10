package com.example.test.demo.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "hr_comics")
public class Comic extends BaseMongoModel{

    @Field(name = "id")
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "another_name")
    private String anotherName;

    @Field(name = "url")
    private String url;

    @Field(name = "image")
    private String image;

    @Field(name = "categories")
    private String categories;

    @Field(name = "view")
    private String view;

    @Field(name = "follow")
    private String follow;

    @Field(name = "description")
    private String description;

    @Field(name = "chapter")
    private Chapter chapter;

    @Field(name = "status")
    private String status;
}
