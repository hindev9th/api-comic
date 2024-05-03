package com.example.test.demo.Models;

import com.example.test.demo.Helpers.CommonHelper;
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
@Document(value = "hr_histories")
public class History {

    @Field(name = "username")
    private String username;

    @Field(name = "comic_id")
    private String comicId;

    @Field(name = "chapter_id")
    private String chapterId;

    @Field(name = "chapter_name")
    private String chapterName;

    @Field(name = "chapter_url")
    private String chapterUrl;

    @Field(name = "comics")
    private List<Comic> comics;

    public String getChapterUrl(){
        return CommonHelper.getEnv("BASE_COMIC_URL").concat(chapterUrl);
    }
}
