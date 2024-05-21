package com.example.test.demo.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(value = "hr_configs")
public class Config extends BaseMongoModel{
    public enum TYPE {
        BASE_CRAWL_URL,
    }

    @Field(name = "type")
    private String type;

    @Field(name = "content")
    private String content;

    @LastModifiedDate
    private Date updated_at;
}
