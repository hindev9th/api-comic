package com.example.test.demo.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "hr_logs")
public class Log extends BaseMongoModel{
    @Field(value = "name")
    private String name;
    @Field(value = "content")
    private String content;
    @Field(value = "address")
    private String address;
}
