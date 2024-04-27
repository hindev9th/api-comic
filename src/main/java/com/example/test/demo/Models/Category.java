package com.example.test.demo.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "hr_categories")
public class Category extends BaseMongoModel{
    @Field(name = "name")
    private String name;
    @Field(name = "title")
    private String title;
    @Field(name = "url")
    private String url;
    @LastModifiedDate
    private Date updatedAt;
}
