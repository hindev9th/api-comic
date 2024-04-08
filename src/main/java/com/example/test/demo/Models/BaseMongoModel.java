package com.example.test.demo.Models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
public class BaseMongoModel {
    @Id
    private String _id;

    @CreatedDate
    private Date created_at;

    @Setter
    @LastModifiedDate
    private Date updated_at;
}
