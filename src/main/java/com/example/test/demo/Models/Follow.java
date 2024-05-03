package com.example.test.demo.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "hr_follows")
public class Follow {

    @Field(name = "username")
    private String username;

    @Field(name = "comic_id")
    private String comicId;

    @Field(name = "comics")
    private List<Comic> comics;

    @Field(name = "histories")
    private List<History> histories;
}
