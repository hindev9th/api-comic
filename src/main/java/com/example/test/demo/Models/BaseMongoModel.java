package com.example.test.demo.Models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
public abstract class BaseMongoModel {
    @Id
    private String _id;

    @CreatedDate
    private Date created_at;


}
