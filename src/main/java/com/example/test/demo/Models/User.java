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
@Document(value = "hr_users")
public class User extends BaseMongoModel{
    @Field(name = "name")
    private String name;

    @Field(name = "username")
    private String username;

    @Field(name = "password")
    private String password;

    @Field(name = "avatar")
    private String avatar;

    @LastModifiedDate
    private Date updated_at;
}
