package com.example.test.demo.Models;

import com.example.test.demo.Helpers.CommonHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

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

    @Field(name = "image_base64")
    private String imageBase64;

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

    @Field(name = "updated_at")
    private Date updatedAt;

    public String getImage(){
        return "https:" + CommonHelper.getEnv("BASE_IMAGE_URL").concat(image);
    }
}
